package com.dongdong.backend.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;

/**
 * @param sender    消息发送者的DD号
 * @param receiver  消息接收者的DD号
 * @param group     是否为群消息，是 - 1，否 - 0
 * @param type      消息类型，取值：ack, text, emoji, image，分别表示确认、文本、表情和图片
 * @param timestamp 消息发送时间戳，用作消息的唯一标识
 * @param payload   消息主体，对应消息类型，分别为：确认的消息时间戳，文本消息内容，表情代码，图片base64编码
 */
public record Message(String sender, String receiver, Integer group, String type, String timestamp,
                      String payload) implements Serializable {

    public static final String TYPE_ACK = "ack";
    public static final String TYPE_TEXT = "text";
    public static final String TYPE_EMOJI = "emoji";
    public static final String TYPE_IMAGE = "image";

    public static String marshal(Message msg) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(msg);
    }

    public static Message unmarshal(String msg) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(msg, Message.class);
    }

}
