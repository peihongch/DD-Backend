package com.dongdong.backend.entity;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class GroupInfoVO {

    private Long groupId;

    private String groupName;

    private List<UserInfoVO> members;
}
