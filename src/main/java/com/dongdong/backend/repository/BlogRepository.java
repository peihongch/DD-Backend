package com.dongdong.backend.repository;


import com.dongdong.backend.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlogRepository extends JpaRepository<Blog,Long>, JpaSpecificationExecutor<Blog> {

    Optional<Blog> findByBlogId(Long Id);

    List<Blog> findByUserIdOrderByTimestamp(long userId);

    List<Blog> findAllByUserIdInOrderByTimestamp(List<Long> ids);

    void deleteByBlogId(long id);

}
