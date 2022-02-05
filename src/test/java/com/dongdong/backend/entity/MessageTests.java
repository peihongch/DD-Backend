package com.dongdong.backend.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;

class MessageTests {

    @Test
    public void marshal() throws JsonProcessingException {
        Message msg = new Message("dd1", "dd2", 0, "text", "202201201200", "this is a message");
        String msgJson = Message.marshal(msg);
        System.out.println(msgJson);
    }

    @Test
    public void unmarshal() throws JsonProcessingException {
        Message msg = new Message("dd1", "dd2", 0, "text", "202201201200", "this is a message");
        String msgJson = Message.marshal(msg);
        Message parsedMsg = Message.unmarshal(msgJson);
        System.out.println(parsedMsg);
    }

}
