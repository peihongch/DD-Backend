package com.dongdong.backend.entity;

import lombok.Data;

@Data
public class Response {

    private int code;
    private Object data;

    public static Response succeed(Object content) {
        Response response = new Response();
        response.setCode(0);
        response.setData(content);
        return response;
    }

    public static Response error(String msg) {
        Response response = new Response();
        response.setCode(1);
        response.setData(msg);
        return response;
    }
}
