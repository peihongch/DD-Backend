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

@Entity
@Table(name="comments")
@Data
@Builder
@AllArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="userId")
    private Long userId;

    @Column(name="blogId")
    private Long blogId;

    @Column(name="context")
    private  String context;

    @Column(name="timestamp")
    private String timestamp;

    public Comment() {

    }
}
