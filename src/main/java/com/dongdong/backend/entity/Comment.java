package com.dongdong.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name="comments")
@Data
@AllArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="comment_id")
    private Long commentId;

    @Column(name="user_id")
    private Long userId;

    @Column(name="blog_id")
    private Long blogId;

    @Column(name="context")
    private String context;

    @Column(name="timestamp")
    private Timestamp timestamp;

    public Comment() {

    }
}
