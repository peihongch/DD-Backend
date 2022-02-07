package com.dongdong.backend.repository;

import java.util.List;
import java.util.Optional;

import com.dongdong.backend.entity.Picture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PictureRepository extends JpaRepository<Picture, Long>, JpaSpecificationExecutor<Picture> {

    Optional<Picture> findByPictureId(long id);

    List<Picture> findAllByPictureIdIn(List<Long> ids);

    void deleteAllByPictureIdIn(List<Long> ids);

    void deleteByPictureId(long id);

    void deleteByBlogId(long blogId);

    List<Picture> findByBlogIdOrderByPictureId(long blogId);
}
