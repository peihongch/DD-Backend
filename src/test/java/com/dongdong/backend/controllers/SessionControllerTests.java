package com.dongdong.backend.controllers;

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;

class SessionControllerTests {
    @Test
    void p2pSendMessage() throws URISyntaxException, InterruptedException {
        WebSocketClient client1 = new WebSocketClient(new URI("ws://localhost:8080/dd/session/dd1"));
        WebSocketClient client2 = new WebSocketClient(new URI("ws://localhost:8080/dd/session/dd2"));

        var thread1 = new Thread(() -> {
            int times = 10;
            while (times-- >= 0) {
                String msg = """
                        {"sender":"%s","receiver":"%s","group":0,"type":"text","timestamp":"%d","payload":"this is a message from %s"}""";
                client1.sendMessage(String.format(msg, "dd1", "dd2", System.currentTimeMillis(), "dd1"));
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread1.setDaemon(true);
        thread1.start();

        var thread2 = new Thread(() -> {
            int times = 10;
            while (times-- >= 0) {
                String msg = """
                        {"sender":"%s","receiver":"%s","group":0,"type":"text","timestamp":"%d","payload":"this is a message from %s"}""";
                client2.sendMessage(String.format(msg, "dd2", "dd1", System.currentTimeMillis(), "dd2"));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread2.setDaemon(true);
        thread2.start();

        thread1.join();
        thread2.join();
    }

    @Test
    void groupSendMessage() throws URISyntaxException, InterruptedException {
        WebSocketClient client1 = new WebSocketClient(new URI("ws://localhost:8080/dd/session/dd1"));
        WebSocketClient client2 = new WebSocketClient(new URI("ws://localhost:8080/dd/session/dd2"));

//        Thread.sleep(1000 * 60 * 10);

        var thread1 = new Thread(() -> {
            int times = 10;
            while (times-- >= 0) {
                String msg = """
                        {"sender":"%s","receiver":"%s","group":1,"type":"text","timestamp":"%d","payload":"this is a group message from %s"}""";
                client1.sendMessage(String.format(msg, "dd1", "test-group", System.currentTimeMillis(), "dd1"));
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread1.setDaemon(true);
        thread1.start();

        var thread2 = new Thread(() -> {
            int times = 10;
            while (times-- >= 0) {
                String msg = """
                        {"sender":"%s","receiver":"%s","group":1,"type":"text","timestamp":"%d","payload":"this is a group message from %s"}""";
                client2.sendMessage(String.format(msg, "dd2", "test-group", System.currentTimeMillis(), "dd2"));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread2.setDaemon(true);
        thread2.start();

        thread1.join();
        thread2.join();
    }
}