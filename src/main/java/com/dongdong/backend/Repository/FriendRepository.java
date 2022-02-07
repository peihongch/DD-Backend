package com.dongdong.backend.Repository;

import com.dongdong.backend.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface FriendRepository extends JpaRepository<Friend,Long> {

    List<Friend> getFriendByUserId(Long userId);

    @Modifying
    @Transactional
    void deleteByUserIdAndFriendId(Long userId, Long friendId);

    @Modifying
    @Transactional
    @Query(value = "update friend_list set black_list=?3 where user_id=?1 and friend_id=?2",nativeQuery = true)
    void setBlackList(Long userId, Long friendId, int black);

    Friend getFriendByUserIdAndFriendId(Long userId,Long friendId);
}
