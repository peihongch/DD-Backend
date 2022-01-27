package com.dongdong.backend.entity;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name="blog")
@Data
@Builder
@AllArgsConstructor
public class Blog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="userId")
    private Long userId;

    @Column(name="timestamp")
    private String timestamp;

    @Column(name="context")
    private String context;

    @Column(name="likes")
    private Long likes;
    public Blog() {

    }
}
