package com.dongdong.backend.Repository;

import java.util.List;
import java.util.Optional;

import com.dongdong.backend.entity.Picture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PictureRepository extends JpaRepository<Picture, Long>, JpaSpecificationExecutor<Picture> {

    Optional<Picture> findByPictureId(long id);

    List<Picture> findAllByPictureIdIn(List<Long> ids);

    @Modifying
    @Transactional
    void deleteAllByPictureIdIn(List<Long> ids);

    @Modifying
    @Transactional
    void deleteByPictureId(long id);

    @Modifying
    @Transactional
    void deleteByBlogId(long blogId);

    List<Picture> findByBlogIdOrderByPictureId(long blogId);
}
