package com.dongdong.backend.entity;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GroupVO {

    private Long groupId;

    private String groupName;

    public GroupVO(Group group) {
        this.groupId = group.getGroupId();
        this.groupName = group.getGroupName();
    }
}
