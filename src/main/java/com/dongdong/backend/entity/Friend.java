package com.dongdong.backend.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@Data
@DynamicInsert
@Table(name="friend_list")
public class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "userId")
    private Long userId;

    @Column(name = "friendId")
    private Long friendId;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "blackList")
    private int black;

    public Friend(){}
}
