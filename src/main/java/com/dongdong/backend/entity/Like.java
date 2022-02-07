package com.dongdong.backend.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name="likes")
@Data
@Builder
@AllArgsConstructor
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="like_id")
    private Long likeId;

    @Column(name="user_id")
    private Long userId;

    @Column(name="blog_id")
    private Long blogId;

    public Like() {

    }
}
