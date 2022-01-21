package com.dongdong.backend.services;

import com.dongdong.backend.entity.Message;
import com.dongdong.backend.utils.SessionPool;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import javax.websocket.EncodeException;
import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * 创建容器网络：docker network create --driver bridge --subnet 192.168.0.0/16 --gateway 192.168.0.1 ddnet
 * Zookeeper 容器启动：docker run -d --network ddnet -e TZ="Asia/Shanghai" -p 2181:2181 --name zookeeper --restart always zookeeper
 * Kafka 容器启动：docker run -d --network ddnet --name kafka -p 9092:9092 -e KAFKA_BROKER_ID=0 -e KAFKA_ZOOKEEPER_CONNECT=zookeeper:2181 -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://127.0.0.1:9092 -e KAFKA_LISTENERS=PLAINTEXT://0.0.0.0:9092 -t wurstmeister/kafka
 */
@Service
public class SessionServiceImpl implements SessionService {

    private static final Logger log = LoggerFactory.getLogger(SessionServiceImpl.class);
    public static final String P2P_MESSAGE_QUEUE = "message.p2p.%s";
    public static final String GROUP_MESSAGE_QUEUE = "message.group.%s";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final AdminClient kafkaAdmin;
    private final Properties kafkaProps;

    //构造器方式注入  kafkaTemplate
    @Autowired
    public SessionServiceImpl(KafkaTemplate<String, String> kafkaTemplate, AdminClient kafkaAdmin, Properties kafkaProps) {
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaAdmin = kafkaAdmin;
        this.kafkaProps = kafkaProps;
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
     * @param key   消息
     * @param value
     * @return 是否成功
     */
    public ListenableFuture<SendResult<String, String>> send(String receiver, String key, String value) {
        return kafkaTemplate.send(topic(receiver), key, value);
    }

    @Override
    public boolean register(String receiver) {
        var topicName = topic(receiver);
        NewTopic topic = new NewTopic(topicName, 1, (short) 1);
        var result = kafkaAdmin.createTopics(List.of(topic));
        try {
            result.values().get(topicName).get();
            return true;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean unregister(String receiver) {
        var topicName = topic(receiver);
        kafkaAdmin.deleteTopics(List.of(topicName));
        return true;
    }

    public String topic(String receiver) {
        return String.format(P2P_MESSAGE_QUEUE, receiver);
    }

    /**
     * 给消息发送者响应ACK消息，表示消息成功发送
     *
     * @param msg ACK响应消息
     */
    public void ack(Message msg) throws EncodeException, IOException {
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
        var consumer = getConsumer(this.topic(receiver));
        Thread msgDispatcher = new Thread(() -> {
            // 检查当前会话是否结束
            while (SessionPool.get(receiver) != null) {
                var records = consumer.poll(Duration.ofMillis(100));
                records.forEach(record -> {
                    // 有消息丢失风险：当调用该接口发送消息过程中，目标session关闭了，则会导致该消息丢失
                    // 解决：将消息重新放入kafka
                    var success = this.receive(record.value());
                    if (!success) {
                        this.send(receiver, record.key(), record.value());
                    }
                });
            }
        });
        msgDispatcher.setDaemon(true);
        msgDispatcher.start();
    }

    @Override
    public boolean receive(String msg) {
        try {
            var message = Message.unmarshal(msg);
            var session = SessionPool.get(message.receiver());
            if (session != null) {
                try {
                    session.getBasicRemote().sendText(msg);
                    return true;
                } catch (IOException e) {
                    log.error("接收消息失败: {}", e.getMessage());
                    e.printStackTrace();
                }
            } else {
                log.error("接收消息失败，目标会话已关闭: {}", message.receiver());
            }
        } catch (JsonProcessingException e) {
            log.error("解析消息失败：{}", e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public KafkaConsumer<String, String> getConsumer(String topic) {
        //创建消息者实例
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(this.kafkaProps);
        //订阅topic的消息
        consumer.subscribe(List.of(topic));
        return consumer;
    }

}
