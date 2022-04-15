package com.dongdong.backend.repository;

import com.dongdong.backend.entity.FriendApply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface FriendApplyRepository extends JpaRepository<FriendApply, Long> {

    @Modifying
    @Transactional
    @Query(value = "update friend_apply set state=:state where user_id=:uid and friend_id=:fid", nativeQuery = true)
    void setState(@Param("uid") Long userId, @Param("fid") Long friendId, @Param("state") int state);

    List<FriendApply> getFriendApplyByFriendId(Long friendId);

    boolean existsByUserIdAndFriendId(Long userId, Long friendId);
}
