package com.dongdong.backend.repository;

import com.dongdong.backend.entity.GroupApply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupApplyRepository extends JpaRepository<GroupApply, Long> {

    List<GroupApply> findByGroupIdInOrderByGroupId(List<Long> ids);

    Optional<GroupApply> findByApplyId(Long id);

    @Modifying
    @Transactional
    void deleteByApplyId(Long id);

    @Modifying
    @Transactional
    void deleteByGroupId(Long id);
}
