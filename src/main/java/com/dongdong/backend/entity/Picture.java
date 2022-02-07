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
@Table(name="pictures")
@Data
@Builder
@AllArgsConstructor
public class Picture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="picture_id")
    private Long pictureId;

    @Column(name="blog_id")
    private Long blogId;

    @Column(name="pic")
    private  String pic;

    public Picture() {

    }
}
