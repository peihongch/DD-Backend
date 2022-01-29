package com.dongdong.backend.services;

import com.dongdong.backend.entity.Message;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;

import javax.websocket.EncodeException;
import java.io.IOException;

public interface SessionService {

    String sessionID(String name);

    String topic(String receiver);

    ListenableFuture<SendResult<String, String>> send(String receiver, String key, String value);

    /**
     * 注册用户时调用，往kafka中创建用户接收消息的topic
     *
     * @param receiver 注册用户的DD号
     * @return 创建成功与否
     */
    boolean register(String receiver);

    /**
     * 注销用户时调用，从kafka中删除用户接收消息的topic
     *
     * @param receiver 注销用户的DD号
     * @return 删除成功与否
     */
    boolean unregister(String receiver);

    void ack(Message msg) throws EncodeException, IOException;

    void dispatch(String receiver);

    boolean receive(String msg);
}
