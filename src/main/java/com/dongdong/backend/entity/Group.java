package com.dongdong.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name="qun")
@NoArgsConstructor
@AllArgsConstructor
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="group_id")
    private Long groupId;

    @Column(name="group_name")
    private String groupName;

    @Column(name="master_id")
    private Long masterId;
}
