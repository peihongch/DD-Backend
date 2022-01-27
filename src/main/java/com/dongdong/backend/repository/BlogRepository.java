package com.dongdong.backend.repository;


import com.dongdong.backend.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogRepository extends JpaRepository<Blog,Long>, JpaSpecificationExecutor<Blog> {

    List<Blog> findByUserIdOrderByTimestamp(long userId);

    List<Blog> findAllByUserIdOrderByTimestamp(List<Long> ids);

    void deleteById(long id);

}
