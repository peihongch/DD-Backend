package com.dongdong.backend.entity;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GroupSearchVO {

    private Long groupId;

    private String groupName;

    private boolean in;
}
