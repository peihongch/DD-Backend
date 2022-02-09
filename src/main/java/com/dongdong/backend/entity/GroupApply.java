package com.dongdong.backend.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name="group_apply")
@NoArgsConstructor
@AllArgsConstructor
public class GroupApply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="apply_id")
    private Long applyId;

    @Column(name="user_id")
    private Long userId;

    @Column(name="group_id")
    private Long groupId;

    @Column(name="reason")
    private String reason;

    @Column(name="state")
    private Long state;

}
