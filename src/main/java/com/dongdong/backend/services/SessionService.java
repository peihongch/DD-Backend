package com.dongdong.backend.services;

import com.dongdong.backend.entity.Message;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;

import javax.websocket.EncodeException;
import java.io.IOException;

public interface SessionService {

    String sessionID(String name);

    ListenableFuture<SendResult<String, String>> send(String receiver, String msg);

    void ack(Message msg) throws EncodeException, IOException;
}
