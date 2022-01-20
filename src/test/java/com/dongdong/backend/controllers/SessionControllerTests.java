package com.dongdong.backend.controllers;

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;

class SessionControllerTests {
    @Test
    void sendMessage() throws InterruptedException, URISyntaxException {
        WebSocketClient client = new WebSocketClient(new URI("ws://localhost:8080/dd/session/myname"));

        while (true) {
            client.sendMessage("Hello");
            Thread.sleep(1000);
        }
    }
}