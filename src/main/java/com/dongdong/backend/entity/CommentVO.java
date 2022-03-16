package com.dongdong.backend.entity;


import lombok.Data;

@Data
public class CommentVO {

    private String userId;

    private String userName;

    private String commentId;

    private String context;

    private String timestamp;

    public CommentVO() {

    }

}
