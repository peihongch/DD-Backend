package com.dongdong.backend.controllers;

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;

class SessionControllerTests {
    @Test
    void sendMessage() throws InterruptedException, URISyntaxException {
        WebSocketClient client = new WebSocketClient(new URI("ws://localhost:8080/dd/session/dd1"));

        int times = 10;
        while (times-- >= 0) {
            String msg = """
                    {"sender":"dd1","receiver":"dd2","type":"text","timestamp":"%d","payload":"this is a message"}""";
            client.sendMessage(String.format(msg, System.currentTimeMillis()));
            Thread.sleep(1000);
        }
    }
}