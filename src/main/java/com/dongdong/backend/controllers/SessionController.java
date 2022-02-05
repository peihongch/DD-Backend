package com.dongdong.backend.controllers;

import com.dongdong.backend.entity.Message;
import com.dongdong.backend.services.SessionService;
import com.dongdong.backend.utils.SessionPool;
import com.dongdong.backend.utils.TimeUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    public static SessionService sessionService;

    @OnOpen
    public void openSession(@PathParam("name") String name, Session session) {
        log.info("有新的连接：{}", session);
        String sessionName = sessionService.sessionID(name);
        SessionPool.add(sessionName, session);
        log.info("用户已上线：{}", name);
        log.info("在线人数：{}", SessionPool.count());
        SessionPool.sessionMap().keySet().forEach(item -> log.info("在线用户：" + item));
        // 测试用 //////////////////////////////
//        sessionService.register("dd1");
//        sessionService.register("dd2");
        //////////////////////////////////////
        sessionService.dispatch(name);
    }

    @OnMessage
    public void onMessage(String message) {
        log.info("有新消息： {}", message);
        try {
            Message msg = Message.unmarshal(message);
            // 发送到kafka
            ListenableFuture<SendResult<String, String>> future = sessionService.send(msg.receiver(), msg.timestamp(), message);
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
            if (throwable.getMessage() != null) {
                log.error("连接出现异常：{}", throwable.getMessage());
            }
            session.close();
        } catch (IOException e) {
            log.error("退出发生异常：{}", e.getMessage());
        }
    }

}
