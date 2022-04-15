package com.dongdong.backend.repository;


import com.dongdong.backend.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long>, JpaSpecificationExecutor<Like> {

    boolean existsByBlogIdAndUserId(Long blogId, Long userId);

    @Modifying
    @Transactional
    void deleteByBlogIdAndUserId(Long blogId, Long userId);

    @Modifying
    @Transactional
    void deleteByBlogId(Long blogId);
}
