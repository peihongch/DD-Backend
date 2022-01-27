package com.dongdong.backend.repository;

import java.util.List;

import com.dongdong.backend.entity.Picture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PictureRepository extends JpaRepository<Picture, Long>, JpaSpecificationExecutor<Picture> {

    Picture findById(long id);

    List<Picture> findAllById(List<Long> ids);

    void deleteAllById(List<Long> ids);

    void deleteById(long id);

    void deleteByBlogId(long blogId);

    List<Picture> findByBlogIdOrderById(long blogId);
}
