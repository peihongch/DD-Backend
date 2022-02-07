package com.dongdong.backend.services;


import com.dongdong.backend.VO.BlogVO;
import com.dongdong.backend.VO.CommentVO;
import com.dongdong.backend.VO.NewBlogVO;
import com.dongdong.backend.entity.*;
import com.dongdong.backend.repository.BlogRepository;
import com.dongdong.backend.repository.CommentRepository;
import com.dongdong.backend.repository.LikeRepository;
import com.dongdong.backend.repository.PictureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
            String blogId=String.valueOf(blog.getBlogId());
            List<Picture> pics=pictureRepository.findByBlogIdOrderByPictureId(Long.valueOf(blogId));
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
            if(likeRepository.existsByBlogIdAndUserId(blog.getBlogId(),blog.getUserId())){
                liked=true;
            }
//            results.add(new BlogVO(userId,blogId,blog.getTimestamp(),blog.getContext(),pictures,comments,blog.getLikes(),liked));
        }

        return results;
    }

    @Override
    public void addBlog(NewBlogVO newBlogVO) {
        Date date=new Date();
        Blog blog=new Blog();
        blog.setContext(newBlogVO.getContext());
        blog.setLikes(Long.valueOf(0));
        blog.setUserId(Long.valueOf(newBlogVO.getUserId()));
        blog.setOwnerId(Long.valueOf(newBlogVO.getUserId()));
        blog.setTimestamp(new Timestamp(date.getTime()));
        blog=blogRepository.save(blog);
        if(newBlogVO.getPics().size()!=0){
            List<Picture> pictures=new ArrayList<>();
            for (String pic:newBlogVO.getPics()){
                Picture picture = new Picture();
                picture.setBlogId(blog.getBlogId());
                picture.setPic(pic);
                pictures.add(picture);
            }
            pictureRepository.saveAll(pictures);
        }

    }

    @Override
    public void deleteBlog(String blogId) {
        pictureRepository.deleteByBlogId(Long.valueOf(blogId));
        commentRepository.deleteByBlogId(Long.valueOf(blogId));
        likeRepository.deleteByBlogId(Long.valueOf(blogId));
        blogRepository.deleteByBlogId(Long.valueOf(blogId));
    }

    @Override
    public void transferBlog(String userId, String blogId) {
        Date date=new Date();
        Blog source=blogRepository.findByBlogId(Long.valueOf(blogId)).get();
        Blog blog = new Blog();
        blog.setTimestamp(new Timestamp(date.getTime()));
        blog.setLikes(Long.valueOf(0));
        blog.setContext(source.getContext());
        blog.setUserId(Long.valueOf(userId));
        blog.setOwnerId(source.getOwnerId());
        blog = blogRepository.save(blog);
        List<Picture> pictures=pictureRepository.findByBlogIdOrderByPictureId(Long.valueOf(blogId));
        List<Picture> newPictures=new ArrayList<>();
        for(Picture picture : pictures){
            Picture pic =new Picture();
            pic.setPic(picture.getPic());
            pic.setBlogId(blog.getBlogId());
            newPictures.add(pic);
        }
        pictureRepository.saveAll(newPictures);

    }

    @Override
    public void likeBlog(String userId, String blogId) {
        Blog blog=blogRepository.findByBlogId(Long.valueOf(blogId)).get();
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
        Blog blog=blogRepository.findByBlogId(Long.valueOf(blogId)).get();
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
    public CommentVO addComment(String userId, String blogId, String context) {
        Comment comment =new Comment();
        comment.setBlogId(Long.valueOf(blogId));
        Date date=new Date();
        comment.setTimestamp(new Timestamp(date.getTime()));
        comment.setUserId(Long.valueOf(userId));
        comment.setContext(context);
        comment=commentRepository.save(comment);
        CommentVO commentVO=new CommentVO();
        commentVO.setCommentId(String.valueOf(comment.getCommentId()));
        commentVO.setContext(comment.getContext());
        commentVO.setTimestamp(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(comment.getTimestamp()));
        commentVO.setUserId(String.valueOf(comment.getUserId()));
        commentVO.setUserName("");
        return commentVO;
    }

    @Override
    public void deleteComment(String commentId) {
        commentRepository.deleteByCommentId(Long.valueOf(commentId));
    }
}
