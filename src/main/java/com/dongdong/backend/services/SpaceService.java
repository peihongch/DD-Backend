package com.dongdong.backend.services;

import com.dongdong.backend.entity.BlogVO;

import java.util.List;

public interface SpaceService {

    List<BlogVO> showAll(String userId);

    List<BlogVO> showBlogs(String ownerId);

    void addBlog(BlogVO blogVO);

    void deleteBlog(String blogId);

    void transferBlog(String userId, String blogId,String timestamp);

    void likeBlog(String userId,String blogId);

    void dislikeBlog(String userId, String blogId);

    void addComments(String userId, String blogId, String context,String timestamp);

    void deleteComents(String contextId);
}
