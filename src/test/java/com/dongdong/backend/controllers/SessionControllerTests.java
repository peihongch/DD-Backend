package com.dongdong.backend.controllers;

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;

class SessionControllerTests {
    @Test
    void sendMessage() throws InterruptedException, URISyntaxException {
        WebSocketClient client1 = new WebSocketClient(new URI("ws://localhost:8080/dd/session/dd1"));
        WebSocketClient client2 = new WebSocketClient(new URI("ws://localhost:8080/dd/session/dd2"));

        int times = 10;
        while (times-- >= 0) {
            String msg = """
                    {"sender":"%s","receiver":"%s","type":"text","timestamp":"%d","payload":"this is a message from %s"}""";
            client1.sendMessage(String.format(msg, "dd1", "dd2", System.currentTimeMillis(), "dd1"));
            client2.sendMessage(String.format(msg, "dd2", "dd1", System.currentTimeMillis(), "dd2"));
            Thread.sleep(1000);
        }
    }
}