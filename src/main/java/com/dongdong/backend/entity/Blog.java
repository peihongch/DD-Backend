package com.dongdong.backend.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "blog")
@Data
@Builder
@AllArgsConstructor
public class Blog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blog_id")
    private Long blogId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "owner_id")
    private Long ownerId;

    @Column(name = "timestamp")
    private Timestamp timestamp;

    @Column(name = "context")
    private String context;

    @Column(name = "likes")
    private int likes;

    public Blog() {

    }
}
