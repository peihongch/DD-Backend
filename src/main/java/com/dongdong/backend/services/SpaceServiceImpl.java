package com.dongdong.backend.services;


import com.dongdong.backend.entity.Blog;
import com.dongdong.backend.entity.BlogVO;
import com.dongdong.backend.entity.Comment;
import com.dongdong.backend.entity.Picture;
import com.dongdong.backend.repository.BlogRepository;
import com.dongdong.backend.repository.CommentRepository;
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
            results.add(new BlogVO(userId,blogId,blog.getTimestamp(),blog.getContext(),pictures,comments,blog.getLikes()));
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
            for (String pic:blogVO.pics()){
                Picture picture = new Picture();
                picture.setBlogId(blog.getId());
                picture.setPic(pic);
                pictureRepository.save(picture);
            }
        }

    }

    @Override
    public void deleteBlog(String blogId) {
        blogRepository.deleteById(Long.valueOf(blogId));
        pictureRepository.deleteByBlogId(Long.valueOf(blogId));
        commentRepository.deleteByBlogId(Long.valueOf(blogId));
    }

    @Override
    public void transferBlog(String userId, String blogId) {

    }

    @Override
    public void likeBlog(String userId, String blogId) {

    }

    @Override
    public void dislikeBlog(String userId, String blogId) {

    }

    @Override
    public void addComments(String userId, String blogId, String context) {

    }

    @Override
    public void deleteComents(String userId, String blogId, String contextId) {

    }
}
