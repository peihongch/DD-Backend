package com.dongdong.backend.controllers;


import com.dongdong.backend.entity.BlogVO;
import com.dongdong.backend.entity.CommentVO;
import com.dongdong.backend.entity.NewBlogVO;
import com.dongdong.backend.entity.Response;
import com.dongdong.backend.services.SpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/space")
public class SpaceController {

    @Autowired
    SpaceService spaceService;

    @GetMapping("/list")
    public Response showAll(@RequestParam String userId){
        List<BlogVO> results=spaceService.showAll(userId);
        if(results==null){
            return Response.error("Error!");
        }
        return Response.succeed(results);
    }

    @GetMapping("/look")
    public Response look(@RequestParam String userId,@RequestParam String friendId){
        List<BlogVO> results= spaceService.showBlogs(userId,friendId);
        if(results==null){
            return Response.error("Error!");
        }
        return Response.succeed(results);
    }

    @PostMapping("/add")
    public Response addBlog(@RequestBody NewBlogVO newBlogVO){
        Long blogId= spaceService.addBlog(newBlogVO);
        if (blogId==null){
            return Response.error("添加失败");
        }
        return Response.succeed(blogId);
    }

    @PostMapping("/delete")
    public Response deleteBlog(@RequestParam String blogId){
        spaceService.deleteBlog(blogId);
        return Response.succeed(null);
    }

    @PostMapping("/like")
    public Response likeBlog(@RequestParam String userId,@RequestParam String blogId){
        Long likeId=spaceService.likeBlog(userId,blogId);
        if(likeId==null){
            return Response.error("错误");
        }
        return Response.succeed(likeId);
    }

    @PostMapping("/dislike")
    public Response dislikeBlog(@RequestParam String userId,@RequestParam String blogId){
        spaceService.dislikeBlog(userId, blogId);
        return Response.succeed(null);
    }

    @PostMapping("/comment")
    public Response addComment(@RequestParam String userId, @RequestParam String blogId, @RequestParam String context){
        CommentVO commentVO = spaceService.addComment(userId, blogId, context);
        if(commentVO==null){
            return Response.error("评论失败");
        }
        return Response.succeed(commentVO);
    }

    @PostMapping("/deleteComment")
    public Response deleteComment(@RequestParam String commentId){
        spaceService.deleteComment(commentId);
        return Response.succeed("删除成功");
    }

    @PostMapping("/transfer")
    public  Response transferComment(@RequestParam String userId, @RequestParam String blogId){
        Long blog=spaceService.transferBlog(userId, blogId);
        if(blog==null){
            return Response.error("转发失败");
        }
        return Response.succeed(blog);
    }

}
