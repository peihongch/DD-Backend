package com.dongdong.backend.entity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;



@Data
@NoArgsConstructor
public class BlogVO {
    private String userId;

    private String userName;

    private String ownerId;

    private String ownerName;

    private String blogId;

    private String timestamp;

    private String context;

    private List<String> pics;

    private List<CommentVO> comments;

    private int likes;

    private boolean liked;
}
