package com.dongdong.backend.services;

import com.dongdong.backend.entity.Message;
import com.dongdong.backend.entity.Operation;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;

import javax.websocket.EncodeException;
import java.io.IOException;
import java.util.List;

public interface SessionService {

    String sessionID(String name);

    String getTopicName(String receiver, boolean isGroup);

    ListenableFuture<SendResult<String, String>> send(String receiver, String key, String value);

    /**
     * 注册用户或者群组时调用，往kafka中创建接收用户/群组消息的topic
     *
     * @param receiver 注册用户/创建群组的DD号
     * @return 创建成功与否
     */
    boolean register(String receiver, boolean isGroup);

    /**
     * 注销用户或者解散群组时调用，从kafka中删除接收用户/群组消息的topic
     *
     * @param receiver 注销用户/解散群组的DD号
     * @return 删除成功与否
     */
    boolean unregister(String receiver, boolean isGroup);

    void ack(Message msg) throws EncodeException, IOException;

    void dispatch(String receiver);

    boolean receive(String msg, String receiver);

    /**
     * 更新消息接收者监听的Kafka Topics列表
     *
     * @param receiver  消息接收者的DD号
     * @param topicName topic名称
     * @param op        操作类型
     */
    void updateKafkaConsumer(String receiver, String topicName, Operation op);
}

