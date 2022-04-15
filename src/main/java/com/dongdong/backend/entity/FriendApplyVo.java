package com.dongdong.backend.entity;

import lombok.Data;


@Data
public class FriendApplyVo {

    private Long userId;

    private String msg;

    private int state;

    private String userName;

    public FriendApplyVo(FriendApply friendApply) {
        this.userId = friendApply.getUserId();
        this.msg = friendApply.getMsg();
        this.state = friendApply.getState();
    }
}
