package com.dongdong.backend.controllers;

import com.dongdong.backend.entity.Message;
import com.dongdong.backend.services.SessionService;
import com.dongdong.backend.entity.Operation;
import com.dongdong.backend.utils.SessionPool;
import com.dongdong.backend.utils.TimeUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@RestController
@RequestMapping("/session/{name}")
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
        // sessionService.register("dd1", false);
        // sessionService.register("dd2", false);
        // sessionService.register("test-group", true);
        //////////////////////////////////////
        sessionService.dispatch(name);
    }

    @OnMessage
    public void onMessage(String message) {
        log.info("有新消息： {}", message);
        try {
            var msg = Message.unmarshal(message);
            // 发送到kafka
            var future = sessionService.send(msg.receiver(), msg.timestamp(), message);
            future.addCallback(result -> {
                log.info("消息暂存成功：{}", message);
                // 响应ack消息
                var ackMsg = new Message(msg.receiver(), msg.sender(), msg.group(), Message.TYPE_ACK, TimeUtils.currentTimestamp(), msg.timestamp());
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

    @PostMapping(value = "/join/{group}")
    public void joinGroup(@PathVariable("name") String name, @PathVariable("group") String group) {
        var topicName = sessionService.getTopicName(group, true);
        sessionService.updateKafkaConsumer(name, topicName, Operation.ADD);
    }

    @PostMapping(value = "/leave/{group}")
    public void leaveGroup(@PathVariable("name") String name, @PathVariable("group") String group) {
        var topicName = sessionService.getTopicName(group, true);
        sessionService.updateKafkaConsumer(name, topicName, Operation.DELETE);
    }

}
