package com.dongdong.backend.services;

import com.dongdong.backend.entity.GroupVO;
import com.dongdong.backend.entity.Message;
import com.dongdong.backend.entity.Operation;
import com.dongdong.backend.utils.SessionPool;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.errors.TopicExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * 创建容器网络：docker network create --driver bridge --subnet 192.168.0.0/16 --gateway 192.168.0.1 ddnet
 * Zookeeper 容器启动：docker run -d --network ddnet -e TZ="Asia/Shanghai" -p 2181:2181 --name zookeeper --restart always zookeeper
 * Kafka 容器启动：docker run -d --network ddnet --name kafka -p 9092:9092 -e KAFKA_BROKER_ID=0 -e KAFKA_ZOOKEEPER_CONNECT=zookeeper:2181 -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://127.0.0.1:9092 -e KAFKA_LISTENERS=PLAINTEXT://0.0.0.0:9092 -t wurstmeister/kafka
 */
@Service
public class SessionServiceImpl implements SessionService {

    public static final String P2P_MESSAGE_QUEUE = "message.p2p.%s";
    public static final String GROUP_MESSAGE_QUEUE = "message.group.%s";
    public static final String KAFKA_CONSUMER_GROUP_ID = "group.id.%s";
    private static final Logger log = LoggerFactory.getLogger(SessionServiceImpl.class);
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final AdminClient kafkaAdmin;
    private final Properties kafkaProps;

    private final GroupService groupService;

    //构造器方式注入  kafkaTemplate
    @Autowired
    public SessionServiceImpl(KafkaTemplate<String, String> kafkaTemplate, AdminClient kafkaAdmin, Properties kafkaProps, GroupService groupService) {
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaAdmin = kafkaAdmin;
        this.kafkaProps = kafkaProps;
        this.groupService = groupService;
    }

    /**
     * 根据key和用户名生成一个key值，简单实现下
     *
     * @param name 发送人
     * @return 返回值
     */
    public String sessionID(String name) {
        return name;
    }

    /**
     * 将消息暂存到Kafka中
     *
     * @param key   消息的键
     * @param value 消息的内容
     * @return 是否成功
     */
    public ListenableFuture<SendResult<String, String>> send(String receiver, String key, String value) {
        var isGroup = false;
        try {
            var msg = Message.unmarshal(value);
            isGroup = msg.group() == 1;
        } catch (JsonProcessingException e) {
            log.error("解析消息失败: {}", e.getMessage());
        }
        var topicName = getTopicName(receiver, isGroup);
        log.info("暂存消息到topic: {}", topicName);
        return kafkaTemplate.send(topicName, key, value);
    }

    @Override
    public boolean register(String receiver, boolean isGroup) {
        var topicName = getTopicName(receiver, isGroup);
        var topic = new NewTopic(topicName, 1, (short) 1);
        try {
            var result = kafkaAdmin.createTopics(List.of(topic));
            result.values().get(topicName).get();
            return true;
        } catch (TopicExistsException e) {
            log.error("会话已存在: {}, {}", topicName, e.getMessage());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean unregister(String receiver, boolean isGroup) {
        var topicName = getTopicName(receiver, isGroup);
        kafkaAdmin.deleteTopics(List.of(topicName));
        return true;
    }

    @Override
    public String getTopicName(String receiver, boolean isGroup) {
        return String.format(isGroup ? GROUP_MESSAGE_QUEUE : P2P_MESSAGE_QUEUE, receiver);
    }

    /**
     * 给消息发送者响应ACK消息，表示消息成功发送
     *
     * @param msg ACK响应消息
     */
    public void ack(Message msg) throws IOException {
        String receiver = msg.receiver();
        Session session = SessionPool.get(receiver);
        if (session != null) {
            RemoteEndpoint.Basic basic = session.getBasicRemote();
            if (basic != null) {
                basic.sendText(Message.marshal(msg));
            } else {
                log.error("获取：{}->{} 会话不存在", msg.receiver(), msg.sender());
            }
        } else {
            log.error("ACK消息：{} 会话不存在", receiver);
        }
    }

    @Override
    public void dispatch(String receiver) {
        // 调用接口获取receiver所属的群组的DD号列表
        var groups = this.groupService.show(Long.valueOf(receiver))
                .parallelStream()
                .map(GroupVO::getGroupId)
                .map(String::valueOf)
                .toList();
        // 测试用的群组
        // groups.add("10000");
        var p2pTopicName = this.getTopicName(receiver, false);
        var topicNames = new ArrayList<>(groups.stream().map(g -> getTopicName(g, true)).toList());
        topicNames.add(p2pTopicName);

        var consumer = getKafkaConsumer(receiver, topicNames);
        Thread msgDispatcher = new Thread(() -> {
            // 检查当前会话是否结束
            log.info("开始接收消息: {}", receiver);
            while (SessionPool.get(receiver) != null) {
                var records = consumer.poll(Duration.ofMillis(100));
                records.forEach(record -> {
                    // 有消息丢失风险：当调用该接口发送消息过程中，目标session关闭了，则会导致该消息丢失
                    // 解决：将消息重新放入kafka
                    var success = this.receive(record.value(), receiver);
                    log.info("接收消息: {}, {}", receiver, success ? "成功" : "失败");
                    if (!success) {
                        this.send(receiver, record.key(), record.value());
                    }
                });
            }
            log.info("停止接收消息: {}", receiver);
        });
        msgDispatcher.setDaemon(true);
        msgDispatcher.start();
    }

    @Override
    public boolean receive(String msg, String receiver) {
        var session = SessionPool.get(receiver);
        if (session != null) {
            try {
                session.getBasicRemote().sendText(msg);
                return true;
            } catch (IOException e) {
                log.error("接收消息失败: {}", e.getMessage());
                e.printStackTrace();
            }
        } else {
            log.error("接收消息失败，目标会话已关闭: {}", receiver);
        }
        return false;
    }

    public KafkaConsumer<String, String> getKafkaConsumer(String receiver, List<String> topicNames) {
        KafkaConsumer<String, String> consumer = SessionPool.getKafkaConsumer(receiver);
        if (consumer == null) {
            //创建消息者实例
            var props = new Properties();
            props.putAll(this.kafkaProps);
            props.put(ConsumerConfig.GROUP_ID_CONFIG, String.format(KAFKA_CONSUMER_GROUP_ID, receiver));

            consumer = new KafkaConsumer<>(props);
            //订阅topic的消息
            consumer.subscribe(topicNames);

            SessionPool.cacheKafkaConsumer(receiver, consumer);
        }
        return consumer;
    }

    public void updateKafkaConsumer(String receiver, String topicName, Operation op) {
        var consumer = SessionPool.getKafkaConsumer(receiver);
        if (consumer == null) {
            return;
        }

        var topicNames = consumer.listTopics().keySet();
        var newTopicNames = new HashSet<String>(topicNames);
        switch (op) {
            case DELETE -> newTopicNames.remove(topicName);
            case ADD -> newTopicNames.add(topicName);
        }

        consumer.subscribe(newTopicNames);
        consumer.listTopics().forEach((key, value) ->
                System.out.println(key + " -> " + value.parallelStream().map(PartitionInfo::topic).toList().toString()));
    }

}
