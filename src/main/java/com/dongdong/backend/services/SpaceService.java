package com.dongdong.backend.services;

import com.dongdong.backend.entity.BlogVO;

import java.util.List;

public interface SpaceService {

    List<BlogVO> showAll(String userId);

    List<BlogVO> showBlogs(String ownerId);

    void addBlog(BlogVO blogVO);

    void deleteBlog(String blogId);

    void transferBlog(String userId, String blogId);

    void likeBlog(String userId,String blogId);

    void dislikeBlog(String userId, String blogId);

    void addComments(String userId, String blogId, String context);

    void deleteComents(String userId, String blogId, String contextId);
}
