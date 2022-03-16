package com.dongdong.backend.repository;


import com.dongdong.backend.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long>, JpaSpecificationExecutor<Group> {

    List<Group> findByGroupIdInOrderByGroupId(List<Long> ids);

    List<Group> findByGroupNameLikeOrderByGroupId(String name);

    List<Group> findByMasterIdOrderByGroupId(Long id);

    Optional<Group> findByGroupId(Long id);

    @Modifying
    @Transactional
    void deleteByGroupId(Long id);

}
