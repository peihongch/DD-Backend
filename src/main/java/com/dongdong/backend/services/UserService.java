package com.dongdong.backend.services;

import com.dongdong.backend.entity.Friend;
import com.dongdong.backend.entity.User;
import com.dongdong.backend.entity.UserVo;
import com.dongdong.backend.repository.FriendRepository;
import com.dongdong.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FriendRepository friendRepository;

    private static final String MASTER_ID="9999";
    private static final String MASTER_NAME="_ADMIN_";

    public Long signInByEmail(String email, String password, String userName) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setUserName(userName);
        User res = userRepository.save(user);
        Friend friend = new Friend();
        friend.setFriendId(Long.parseLong(MASTER_ID));
        friend.setUserId(res.getUserId());
        friend.setBlack(0);
        friend.setNickname(MASTER_NAME);
        friendRepository.save(friend);
        return res.getUserId();
    }

    public Long signInByPhone(String phone, String password, String userName) {
        User user = new User();
        user.setPhone(phone);
        user.setPassword(password);
        user.setUserName(userName);
        User res = userRepository.save(user);
        Friend friend = new Friend();
        friend.setFriendId(Long.parseLong(MASTER_ID));
        friend.setUserId(res.getUserId());
        friend.setBlack(0);
        friend.setNickname(MASTER_NAME);
        friendRepository.save(friend);
        return res.getUserId();
    }

    public User login(String userId, String password) {
        try {
            Long id = Long.parseLong(userId);
            Optional<User> opt = userRepository.findById(id);
            User user = opt.get();
            if (user.getPassword().equals(password)) {
                return user;
            } else {
                return null;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public UserVo getUser(String userId, Long uid) {
        try {
            Long id = Long.parseLong(userId);
            Optional<User> opt = userRepository.findById(id);
            User user = opt.get();
            UserVo userVo = new UserVo(user);
            Friend friend = friendRepository.getFriendByUserIdAndFriendId(uid, id);
            if (friend != null) {
                userVo.setFriend(true);
                userVo.setBlacked(friend.getBlack() == 1);
            }
            return userVo;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<UserVo> searchUser(String username, Long id) {
        try {
            List<User> users = userRepository.findByUserNameLike("%" + username + "%");
            List<UserVo> res = new ArrayList<>();
            for (User user : users) {
                UserVo userVo = new UserVo(user);
                Friend friend = friendRepository.getFriendByUserIdAndFriendId(id, user.getUserId());
                if (friend != null) {
                    userVo.setFriend(true);
                    userVo.setBlacked(friend.getBlack() == 1);
                }
                res.add(userVo);
            }
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public boolean setUser(String userId, String userName, String email, String phone, int age, int gender) {
        try {
            User user = new User();
            user.setUserId(Long.parseLong(userId));
            user.setUserName(userName);
            user.setEmail(email);
            user.setPhone(phone);
            user.setAge(age);
            user.setGender(gender);
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}