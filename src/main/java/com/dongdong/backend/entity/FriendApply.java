package com.dongdong.backend.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@DynamicInsert
@Table(name="friend_apply")
public class FriendApply {

    @Id
    @Column(name = "userId")
    private Long userId;

    @Column(name = "friendId")
    private Long friendId;

    @Column(name = "msg")
    private String msg;

    @Column(name = "state")
    private int state;

    public FriendApply(){}
}
