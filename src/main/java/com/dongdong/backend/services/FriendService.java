package com.dongdong.backend.services;

import com.dongdong.backend.Repository.FriendApplyRepository;
import com.dongdong.backend.Repository.FriendRepository;
import com.dongdong.backend.Repository.UserRepository;
import com.dongdong.backend.entity.Friend;
import com.dongdong.backend.entity.FriendApply;
import com.dongdong.backend.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class FriendService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendRepository friendRepository;

    @Autowired
    private FriendApplyRepository friendApplyRepository;

    public boolean apply(String userId, String friendId, String msg) {
        try {
            Long id = Long.parseLong(userId);
            Long fid = Long.parseLong(friendId);
            if (userRepository.existsById(id) && userRepository.existsById(fid)) {
                FriendApply friendApply = new FriendApply();
                friendApply.setUserId(id);
                friendApply.setFriendId(fid);
                friendApply.setMsg(msg);
                friendApply.setState(0);
                friendApplyRepository.save(friendApply);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Transactional
    public boolean accept(String userId, String friendId) {
        try {
            Long id = Long.parseLong(userId);
            Long fid = Long.parseLong(friendId);
            friendApplyRepository.setState(id, fid, 1);
            Optional<User> opt = userRepository.findById(id);
            User user = opt.get();
            opt = userRepository.findById(fid);
            User fri = opt.get();
            Friend friend = new Friend();
            friend.setFriendId(fid);
            friend.setUserId(id);
            friend.setBlack(0);
            friend.setNickname(fri.getUserName());
            friendRepository.save(friend);
            friend = new Friend();
            friend.setFriendId(id);
            friend.setUserId(fid);
            friend.setBlack(0);
            friend.setNickname(user.getUserName());
            friendRepository.save(friend);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public boolean refuse(String userId, String friendId) {
        try {
            Long id = Long.parseLong(userId);
            Long fid = Long.parseLong(friendId);
            friendApplyRepository.setState(id, fid, 2);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public List<Friend> getFriendList(String userId) {
        try {
            Long id = Long.parseLong(userId);
            List<Friend> friends = friendRepository.getFriendByUserId(id);
            return friends;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Transactional
    public boolean delete(String userId, String friendId) {
        try {
            Long id = Long.parseLong(userId);
            Long fid = Long.parseLong(friendId);
            friendRepository.deleteByUserIdAndFriendId(id, fid);
            friendRepository.deleteByUserIdAndFriendId(fid, id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    @Transactional
    public boolean black(String userId, String friendId) {
        try {
            Long id = Long.parseLong(userId);
            Long fid = Long.parseLong(friendId);
            friendRepository.setBlackList(id, fid, 1);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Transactional
    public boolean deblack(String userId, String friendId) {
        try {
            Long id = Long.parseLong(userId);
            Long fid = Long.parseLong(friendId);
            friendRepository.setBlackList(id, fid, 0);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<FriendApply> getApplyList(String userId) {
        try {
            Long id = Long.parseLong(userId);
            List<FriendApply> friends = friendApplyRepository.getFriendApplyByFriendId(id);
            return friends;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
