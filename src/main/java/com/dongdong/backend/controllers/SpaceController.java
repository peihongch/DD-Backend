package com.dongdong.backend.controllers;


import com.dongdong.backend.VO.CommentVO;
import com.dongdong.backend.VO.NewBlogVO;
import com.dongdong.backend.services.SpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/space")
public class SpaceController {

    @Autowired
    SpaceService spaceService;


    @PostMapping("/add")
    public void addBlog(@RequestBody NewBlogVO newBlogVO){
        spaceService.addBlog(newBlogVO);
    }

    @PostMapping("/delete")
    public void deleteBlog(@RequestParam String blogId){
        spaceService.deleteBlog(blogId);
    }

    @PostMapping("/like")
    public void likeBlog(@RequestParam String userId,@RequestParam String blogId){
        spaceService.likeBlog(userId,blogId);
    }

    @PostMapping("/dislike")
    public void dislikeBlog(@RequestParam String userId,@RequestParam String blogId){
        spaceService.dislikeBlog(userId, blogId);
    }

    @PostMapping("/comment")
    public CommentVO addComment(@RequestParam String userId, @RequestParam String blogId, @RequestParam String context, @RequestParam String timestamp){
        return spaceService.addComment(userId, blogId, context);
    }

    @PostMapping("/deleteComment")
    public void deleteComment(@RequestParam String commentId){
        spaceService.deleteComment(commentId);
    }

    @PostMapping("/transfer")
    public void transferComment(@RequestParam String userId, @RequestParam String blogId, @RequestParam String timestamp){
        spaceService.transferBlog(userId, blogId);
    }

}
