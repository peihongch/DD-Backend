package com.dongdong.backend.controllers;

import com.dongdong.backend.entity.Message;
import com.dongdong.backend.services.SessionService;
import com.dongdong.backend.utils.SessionPool;
import com.dongdong.backend.utils.TimeUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Controller;
import org.springframework.util.concurrent.ListenableFuture;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@Controller
@ServerEndpoint("/session/{name}")
public class SessionController {

    private static final Logger log = LoggerFactory.getLogger(SessionController.class);
    private final SessionService sessionService;

    @Autowired
    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
        Runnable dispatcher = new MessageDispatcher(sessionService);
        Thread dispatchThread = new Thread(dispatcher);
        dispatchThread.setDaemon(true);
        dispatchThread.start();
    }

    @OnOpen
    public void openSession(@PathParam("name") String name, Session session) {
        log.info("有新的连接：{}", session);
        SessionPool.add(sessionService.sessionID(name), session);
        log.info("用户已上线：{}", name);
        log.info("在线人数：{}", SessionPool.count());
        SessionPool.sessionMap().keySet().forEach(item -> log.info("在线用户：" + item));
    }

    @OnMessage
    public void onMessage(String message) {
        log.info("有新消息： {}", message);
        try {
            Message msg = Message.unmarshal(message);
            // 发送到kafka
            ListenableFuture<SendResult<String, String>> future = sessionService.send(msg.receiver(), message);
            future.addCallback(result -> {
                log.info("消息暂存成功：{}", message);
                // 响应ack消息
                Message ackMsg = new Message(msg.receiver(), msg.sender(), Message.TYPE_ACK, TimeUtils.currentTimestamp(), msg.timestamp());
                try {
                    sessionService.ack(ackMsg);
                    log.info("ACK消息发送成功: {}", ackMsg);
                } catch (EncodeException | IOException e) {
                    log.error("ACK消息发送异常，异常情况: {}, {}", ackMsg, e.getMessage());
                    e.printStackTrace();
                }
            }, ex -> log.error("消息暂存失败：{}, {}", message, ex.getMessage()));
        } catch (JsonProcessingException e) {
            log.error("解析消息失败：{}", e.getMessage());
        }
    }

    @OnClose
    public void closeSession(@PathParam("name") String name, Session session) {
        log.info("连接关闭： {}", session);
        SessionPool.remove(sessionService.sessionID(name));
        log.info("在线人数：{}", SessionPool.count());
        SessionPool.sessionMap().keySet().forEach(item -> log.info("在线用户：" + item));
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        try {
            session.close();
        } catch (IOException e) {
            log.error("退出发生异常：{}", e.getMessage());
        }
        log.error("连接出现异常：{}", throwable.getMessage());
    }

}

class MessageDispatcher implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(SessionController.class);
    private final SessionService sessionService;

    public MessageDispatcher(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public void run() {

    }

}
