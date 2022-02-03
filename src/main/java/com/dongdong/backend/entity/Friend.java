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
@Table(name="friend_list")
public class Friend {

    @Column(name = "userId")
    private Long userId;

    @Column(name = "friendId")
    private Long friendId;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "blackList")
    private int blackList;

    public Friend(){}
}
