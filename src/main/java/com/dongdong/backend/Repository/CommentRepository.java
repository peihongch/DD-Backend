package com.dongdong.backend.Repository;


import com.dongdong.backend.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>, JpaSpecificationExecutor<Comment> {

    Comment findByCommentId(long id);

    List<Comment> findAllByCommentIdIn(List<Long> ids);
    @Modifying
    @Transactional
    void deleteByCommentId(long id);
    @Modifying
    @Transactional
    void deleteAllByCommentIdIn(List<Long> ids);
    @Modifying
    @Transactional
    void deleteByBlogId(long blogId);

    List<Comment> findByBlogIdOrderByTimestamp(long blogId);
}
