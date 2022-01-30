package com.dongdong.backend.services;


import com.dongdong.backend.entity.*;
import com.dongdong.backend.repository.BlogRepository;
import com.dongdong.backend.repository.CommentRepository;
import com.dongdong.backend.repository.LikeRepository;
import com.dongdong.backend.repository.PictureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SpaceServiceImpl implements SpaceService {

    @Autowired
    BlogRepository blogRepository;
    @Autowired
    LikeRepository likeRepository;
    @Autowired
    PictureRepository pictureRepository;
    @Autowired
    CommentRepository commentRepository;

    @Override
    public List<BlogVO> showAll(String userId) {

        return null;
    }

    @Override
    public List<BlogVO> showBlogs(String id) {
        List<BlogVO> results=new ArrayList<>();
        List<Blog> blogs=blogRepository.findByUserIdOrderByTimestamp(Long.valueOf(id));
        for(Blog blog : blogs){
            String userId=String.valueOf(blog.getUserId());
            String blogId=String.valueOf(blog.getId());
            List<Picture> pics=pictureRepository.findByBlogIdOrderById(Long.valueOf(blogId));
            List<Comment> coms=commentRepository.findByBlogIdOrderByTimestamp(Long.valueOf(blogId));
            List<String> comments= new ArrayList<>();
            List<String> pictures= new ArrayList<>();
            if (comments.size()!=0){
                for (Comment comment :coms){
                    comments.add(comment.getContext());
                }
            }
            if (pictures.size()!=0){
                for (Picture picture :pics){
                    pictures.add(picture.getPic());
                }
            }
            boolean liked=false;
            if(likeRepository.existsByBlogIdAndUserId(blog.getId(),blog.getUserId())){
                liked=true;
            }
            results.add(new BlogVO(userId,blogId,blog.getTimestamp(),blog.getContext(),pictures,comments,blog.getLikes(),liked));
        }

        return results;
    }

    @Override
    public void addBlog(BlogVO blogVO) {
        Blog blog=new Blog();
        blog.setContext(blogVO.context());
        blog.setLikes(blogVO.likes());
        blog.setUserId(Long.valueOf(blogVO.user()));
        blog.setTimestamp(blogVO.timestamp());
        blog=blogRepository.save(blog);
        if(blogVO.pics().size()!=0){
            List<Picture> pictures=new ArrayList<>();
            for (String pic:blogVO.pics()){
                Picture picture = new Picture();
                picture.setBlogId(blog.getId());
                picture.setPic(pic);
                pictures.add(picture);
            }
            pictureRepository.saveAll(pictures);
        }

    }

    @Override
    public void deleteBlog(String blogId) {
        blogRepository.deleteById(Long.valueOf(blogId));
        pictureRepository.deleteByBlogId(Long.valueOf(blogId));
        commentRepository.deleteByBlogId(Long.valueOf(blogId));
        likeRepository.deleteByBlogId(Long.valueOf(blogId));
    }

    @Override
    public void transferBlog(String userId, String blogId,String timestamp) {
        Blog source=blogRepository.findById(Long.valueOf(blogId)).get();
        Blog blog = new Blog();
        blog.setTimestamp(timestamp);
        blog.setLikes(Long.valueOf(0));
        blog.setContext(source.getContext());
        blog.setUserId(Long.valueOf(userId));
        blog = blogRepository.save(blog);
        List<Picture> pictures=pictureRepository.findByBlogIdOrderById(Long.valueOf(blogId));

    }

    @Override
    public void likeBlog(String userId, String blogId) {
        Blog blog=blogRepository.findById(Long.valueOf(blogId)).get();
        long likes=blog.getLikes();
        likes++;
        blog.setLikes(likes);
        blogRepository.save(blog);
        Like like=new Like();
        like.setBlogId(Long.valueOf(blogId));
        like.setUserId(Long.valueOf(userId));
        likeRepository.save(like);
    }

    @Override
    public void dislikeBlog(String userId, String blogId) {
        likeRepository.deleteByBlogIdAndUserId(Long.valueOf(blogId),Long.valueOf(userId));
        Blog blog=blogRepository.findById(Long.valueOf(blogId)).get();
        long likes=blog.getLikes();
        if(likes==0){
            likes=0;
        }
        else{
            likes--;
        }
        blog.setLikes(likes);
        blogRepository.save(blog);
    }

    @Override
    public void addComments(String userId, String blogId, String context,String timestamp) {
        Comment comment =new Comment();
        comment.setBlogId(Long.valueOf(blogId));
        comment.setTimestamp(timestamp);
        comment.setUserId(Long.valueOf(userId));
        comment.setContext(context);
        commentRepository.save(comment);
    }

    @Override
    public void deleteComents(String contextId) {
        commentRepository.deleteById(Long.valueOf(contextId));
    }
}
