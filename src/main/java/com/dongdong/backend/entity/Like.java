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
    @Column(name="id")
    private Long id;

    @Column(name="userId")
    private Long userId;

    @Column(name="blogId")
    private Long blogId;

    public Like() {

    }
}
