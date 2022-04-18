package com.dongdong.backend.repository;

import com.dongdong.backend.entity.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {

    List<UserGroup> findByUserId(Long userId);

    boolean existsByUserIdAndGroupId(Long userId, Long groupId);

    List<UserGroup> findByGroupId(Long id);

    Optional<UserGroup> findByUserIdAndGroupId(Long userId,Long groupId);

    @Modifying
    @Transactional
    void deleteByUserIdAndGroupId(Long userId, Long groupId);

    @Modifying
    @Transactional
    void deleteByGroupId(Long groupId);


}
