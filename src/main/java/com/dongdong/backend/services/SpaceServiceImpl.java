package com.dongdong.backend.services;


import com.dongdong.backend.entity.*;
import com.dongdong.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
    @Autowired
    UserRepository userRepository;
    @Autowired
    FriendRepository friendRepository;


    @Override
    public List<BlogVO> showAll(String userId) {
        List<Friend> friends = friendRepository.getFriendByUserId(Long.valueOf(userId));
        List<Long> ids = new ArrayList<>();
        for (Friend friend : friends) {
            ids.add(Long.valueOf(friend.getFriendId()));
        }
        ids.add(Long.valueOf(userId));
        List<Blog> blogs = blogRepository.findAllByUserIdInOrderByTimestamp(ids);
        List<BlogVO> results = new ArrayList<>();
        for (Blog blog : blogs) {
            String blogId = String.valueOf(blog.getBlogId());
            String ownerId = String.valueOf(blog.getOwnerId());
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(blog.getTimestamp());
            User user = userRepository.findByUserId(blog.getUserId()).get();
            User owner = userRepository.findByUserId(blog.getOwnerId()).get();
            List<Picture> pics = pictureRepository.findByBlogIdOrderByPictureId(Long.valueOf(blogId));
            List<Comment> coms = commentRepository.findByBlogIdOrderByTimestamp(Long.valueOf(blogId));
            List<CommentVO> comments = new ArrayList<>();
            List<String> pictures = new ArrayList<>();
            if (coms.size() != 0) {
                for (Comment comment : coms) {
                    CommentVO commentVO = new CommentVO();
                    commentVO.setCommentId(String.valueOf(comment.getCommentId()));
                    commentVO.setContext(comment.getContext());
                    commentVO.setTimestamp(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(comment.getTimestamp()));
                    commentVO.setUserId(String.valueOf(comment.getUserId()));
                    User commenter = userRepository.findByUserId(Long.valueOf(comment.getUserId())).get();
                    commentVO.setUserName(commenter.getUserName());
                    comments.add(commentVO);
                }
            }
            if (pictures.size() != 0) {
                for (Picture picture : pics) {
                    pictures.add(picture.getPic());
                }
            }
            boolean liked = likeRepository.existsByBlogIdAndUserId(blog.getBlogId(), Long.valueOf(userId));
            BlogVO blogVO = new BlogVO();
            blogVO.setUserId(String.valueOf(blog.getUserId()));
            blogVO.setUserName(user.getUserName());
            blogVO.setOwnerId(ownerId);
            blogVO.setOwnerName(owner.getUserName());
            blogVO.setBlogId(blogId);
            blogVO.setPics(pictures);
            blogVO.setComments(comments);
            blogVO.setLikes(blog.getLikes());
            blogVO.setLiked(liked);
            blogVO.setTimestamp(timestamp);
            blogVO.setContext(blog.getContext());
            results.add(blogVO);
        }

        return results;
    }

    @Override
    public List<BlogVO> showBlogs(String userId,String friendId) {
        User user = userRepository.findByUserId(Long.valueOf(friendId)).get();
        List<BlogVO> results = new ArrayList<>();
        List<Blog> blogs = blogRepository.findByUserIdOrderByTimestamp(Long.valueOf(friendId));
        for(Blog blog : blogs){
            String blogId = String.valueOf(blog.getBlogId());
            String ownerId = String.valueOf(blog.getOwnerId());
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(blog.getTimestamp());
            User owner = userRepository.findByUserId(Long.valueOf(ownerId)).get();
            List<Picture> pics = new ArrayList<>();
            try {
                pics = pictureRepository.findByBlogIdOrderByPictureId(Long.valueOf(blogId));
            }
            catch (Exception e){
                System.out.println("获取图片失败");
            }
            List<Comment> coms = commentRepository.findByBlogIdOrderByTimestamp(Long.valueOf(blogId));
            List<CommentVO> comments = new ArrayList<>();
            List<String> pictures = new ArrayList<>();
            if (coms.size() != 0){
                for (Comment comment : coms){
                    CommentVO commentVO = new CommentVO();
                    commentVO.setCommentId(String.valueOf(comment.getCommentId()));
                    commentVO.setContext(comment.getContext());
                    commentVO.setTimestamp(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(comment.getTimestamp()));
                    commentVO.setUserId(String.valueOf(comment.getUserId()));
                    User commenter = userRepository.findByUserId(Long.valueOf(comment.getUserId())).get();
                    commentVO.setUserName(commenter.getUserName());
                    comments.add(commentVO);
                }
            }
            if (pictures.size() != 0) {
                for (Picture picture : pics) {
                    pictures.add(picture.getPic());
                }
            }
            boolean liked = likeRepository.existsByBlogIdAndUserId(blog.getBlogId(), Long.valueOf(userId));
            BlogVO blogVO = new BlogVO();
            blogVO.setUserId(friendId);
            blogVO.setUserName(user.getUserName());
            blogVO.setOwnerId(ownerId);
            blogVO.setOwnerName(owner.getUserName());
            blogVO.setBlogId(blogId);
            blogVO.setPics(pictures);
            blogVO.setComments(comments);
            blogVO.setLikes(blog.getLikes());
            blogVO.setLiked(liked);
            blogVO.setTimestamp(timestamp);
            blogVO.setContext(blog.getContext());
            results.add(blogVO);
        }

        return results;
    }

    @Override
    public Long addBlog(NewBlogVO newBlogVO) {
        Calendar cal = Calendar.getInstance();
        Date date = new Date();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, 8);
        date = cal.getTime();
        Blog blog = new Blog();

        blog.setContext(newBlogVO.getContext());
        blog.setLikes(0);
        blog.setUserId(Long.valueOf(newBlogVO.getUserId()));
        blog.setOwnerId(Long.valueOf(newBlogVO.getUserId()));
        blog.setTimestamp(new Timestamp(date.getTime()));
        blog = blogRepository.save(blog);
        if (newBlogVO.getPics().size() != 0) {
            List<Picture> pictures = new ArrayList<>();
            for (String pic : newBlogVO.getPics()) {
                Picture picture = new Picture();
                picture.setBlogId(blog.getBlogId());
                picture.setPic(pic);
                pictures.add(picture);
            }
            pictureRepository.saveAll(pictures);
        }
        return blog.getBlogId();

    }

    @Override
    public void deleteBlog(String blogId) {
        pictureRepository.deleteByBlogId(Long.valueOf(blogId));
        commentRepository.deleteByBlogId(Long.valueOf(blogId));
        likeRepository.deleteByBlogId(Long.valueOf(blogId));
        blogRepository.deleteByBlogId(Long.valueOf(blogId));
    }

    @Override
    public Long transferBlog(String userId, String blogId) {
        Date date = new Date();
        Blog source = blogRepository.findByBlogId(Long.valueOf(blogId)).get();
        Blog blog = new Blog();
        blog.setTimestamp(new Timestamp(date.getTime()));
        blog.setLikes(0);
        blog.setContext(source.getContext());
        blog.setUserId(Long.valueOf(userId));
        blog.setOwnerId(source.getOwnerId());
        blog = blogRepository.save(blog);
        List<Picture> pictures = pictureRepository.findByBlogIdOrderByPictureId(Long.valueOf(blogId));
        List<Picture> newPictures = new ArrayList<>();
        for (Picture picture : pictures) {
            Picture pic = new Picture();
            pic.setPic(picture.getPic());
            pic.setBlogId(blog.getBlogId());
            newPictures.add(pic);
        }
        pictureRepository.saveAll(newPictures);
        return blog.getBlogId();
    }

    @Override
    public Long likeBlog(String userId, String blogId) {
        Blog blog = blogRepository.findByBlogId(Long.valueOf(blogId)).get();
        int likes = blog.getLikes();
        likes++;
        blog.setLikes(likes);
        blogRepository.save(blog);
        Like like = new Like();
        like.setBlogId(Long.valueOf(blogId));
        like.setUserId(Long.valueOf(userId));
        like = likeRepository.save(like);
        return like.getLikeId();
    }

    @Override
    public void dislikeBlog(String userId, String blogId) {
        likeRepository.deleteByBlogIdAndUserId(Long.valueOf(blogId), Long.valueOf(userId));
        Blog blog = blogRepository.findByBlogId(Long.valueOf(blogId)).get();
        int likes = blog.getLikes();
        if (likes == 0) {
            likes = 0;
        } else {
            likes--;
        }
        blog.setLikes(likes);
        blogRepository.save(blog);
    }

    @Override
    public CommentVO addComment(String userId, String blogId, String context) {
        Comment comment = new Comment();
        comment.setBlogId(Long.valueOf(blogId));
        Date date = new Date();
        comment.setTimestamp(new Timestamp(date.getTime()));
        comment.setUserId(Long.valueOf(userId));
        comment.setContext(context);
        comment = commentRepository.save(comment);
        CommentVO commentVO = new CommentVO();
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
