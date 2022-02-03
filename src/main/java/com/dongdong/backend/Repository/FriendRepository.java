package com.dongdong.backend.Repository;

import com.dongdong.backend.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRepository extends JpaRepository<Friend,Long> {

    List<Friend> getFriendByUserId(Long userId);

    @Modifying
    @Query("delete from friend where userId=?1 and friendId=?2")
    void delete(Long userId, Long friendId);

    @Modifying
    @Query("update friend set black_list=?3 where userId=?1 and friendId=?2")
    void setBlackList(Long userId, Long friendId, int black);
}
