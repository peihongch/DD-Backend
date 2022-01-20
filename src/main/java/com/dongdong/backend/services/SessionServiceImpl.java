package com.dongdong.backend.services;

import com.dongdong.backend.entity.Message;
import com.dongdong.backend.utils.SessionPool;
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

/**
 * Zookeeper 容器启动：docker run -d -e TZ="Asia/Shanghai" -p 2181:2181  --name zookeeper --restart always zookeeper
 * Kafka 容器启动：docker run -d --name kafka -p 9092:9092 -e KAFKA_BROKER_ID=0 -e KAFKA_ZOOKEEPER_CONNECT=127.0.0.1:2181 -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://127.0.0.1:9092 -e KAFKA_LISTENERS=PLAINTEXT://0.0.0.0:9092 -t wurstmeister/kafka
 */
@Service
public class SessionServiceImpl implements SessionService {

    private static final Logger log = LoggerFactory.getLogger(SessionServiceImpl.class);
    public static final String P2P_MESSAGE_QUEUE = "/message/p2p/%s";
    public static final String GROUP_MESSAGE_QUEUE = "/message/group/%s";

    private final KafkaTemplate<String, String> kafkaTemplate;

    //构造器方式注入  kafkaTemplate
    @Autowired
    public SessionServiceImpl(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
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
     * @param msg 消息
     * @return 是否成功
     */
    public ListenableFuture<SendResult<String, String>> send(String receiver, String msg) {
        return kafkaTemplate.send(topic(receiver), msg);
    }

    private String topic(String receiver) {
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
                basic.sendObject(msg);
            } else {
                log.error("获取：{}->{} 会话不存在", msg.receiver(), msg.sender());
            }
        } else {
            log.error("ACK消息：{} 会话不存在", receiver);
        }
    }

}
