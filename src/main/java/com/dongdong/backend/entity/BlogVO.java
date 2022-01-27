package com.dongdong.backend.entity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @param user      动态发表者的id
 * @param blogId    Blog的id
 * @param timestamp 时间
 * @param context   动态的文字内容
 * @param pics      动态的图片
 * @param comments  动态的评论
 * @param likes     点赞数
 */
public record BlogVO(String user, String blogId,String timestamp, String context, List<String> pics, List<String> comments, Long likes)
        implements Serializable{

    public static String marshal(BlogVO blogVO) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(blogVO);
    }

    public static BlogVO unmarshal(String blogVO) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(blogVO, BlogVO.class);
    }
}
