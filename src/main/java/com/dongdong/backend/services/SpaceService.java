package com.dongdong.backend.services;

import com.dongdong.backend.entity.BlogVO;
import com.dongdong.backend.entity.CommentVO;
import com.dongdong.backend.entity.NewBlogVO;

import java.util.List;

public interface SpaceService {

    List<BlogVO> showAll(String userId);

    List<BlogVO> showBlogs(String userId, String friendId);

    Long addBlog(NewBlogVO newBlogVO);

    void deleteBlog(String blogId);

    Long transferBlog(String userId, String blogId);

    Long likeBlog(String userId, String blogId);

    void dislikeBlog(String userId, String blogId);

    CommentVO addComment(String userId, String blogId, String context);

    void deleteComment(String commentId);
}
