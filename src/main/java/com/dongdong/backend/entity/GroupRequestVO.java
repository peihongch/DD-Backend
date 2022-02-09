package com.dongdong.backend.entity;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GroupRequestVO {

    private Long groupRequestId;

    private UserInfoVO user;

    private GroupVO group;

    private String reason;
}
