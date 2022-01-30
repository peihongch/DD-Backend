package com.dongdong.backend.repository;


import com.dongdong.backend.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like,Long>, JpaSpecificationExecutor<Like> {

    boolean existsByBlogIdAndUserId(Long blogId,Long userId);

    void deleteByBlogIdAndUserId(Long blogId,Long userId);

    void deleteByBlogId(Long blogId);
}
