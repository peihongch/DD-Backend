package com.dongdong.backend.repository;


import com.dongdong.backend.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>, JpaSpecificationExecutor<Comment> {

    Comment findById(long id);

    List<Comment> findAllById(List<Long> ids);

    void deleteById(long id);

    void deleteAllById(List<Long> ids);

    void deleteByBlogId(long blogId);

    List<Comment> findByBlogIdOrderByTimestamp(long blogId);
}
