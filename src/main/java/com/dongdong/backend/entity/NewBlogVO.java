package com.dongdong.backend.entity;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class NewBlogVO {

    private String userId;

    private String context;

    private List<String> pics;
}
