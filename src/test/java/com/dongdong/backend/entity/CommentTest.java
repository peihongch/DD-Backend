package com.dongdong.backend.entity;

import com.dongdong.backend.Repository.CommentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CommentTest {
    @Autowired
    private CommentRepository commentRepository;

    @Test
    public void addComment(){
        Date date=new Date();
        Comment comment =new Comment();
        comment.setContext("");
        comment.setTimestamp(new Timestamp(date.getTime()));
        comment.setBlogId(Long.valueOf(1));
        comment.setUserId(Long.valueOf(1));
        comment=commentRepository.save(comment);
        comment=commentRepository.findByCommentId(comment.getCommentId());
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(comment.getTimestamp()));
        System.out.println(comment.getCommentId());
        commentRepository.delete(comment);
    }

}