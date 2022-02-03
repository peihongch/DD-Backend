package com.dongdong.backend.Repository;

import com.dongdong.backend.entity.FriendApply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendApplyRepository extends JpaRepository<FriendApply,Long> {

    @Modifying
    @Query("update friend_apply set state=?3 where userId=?1 and friendId=?2")
    int updateState(Long userId,Long friendId,int state);

}
