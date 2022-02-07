package com.dongdong.backend.services;

import com.dongdong.backend.VO.BlogVO;
import com.dongdong.backend.VO.CommentVO;
import com.dongdong.backend.VO.NewBlogVO;

import java.util.List;

public interface SpaceService {

    List<BlogVO> showAll(String userId);

    List<BlogVO> showBlogs(String ownerId);

    void addBlog(NewBlogVO newBlogVO);

    void deleteBlog(String blogId);

    void transferBlog(String userId, String blogId);

    void likeBlog(String userId,String blogId);

    void dislikeBlog(String userId, String blogId);

    CommentVO addComment(String userId, String blogId, String context);

    void deleteComment(String commentId);
}
