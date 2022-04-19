package com.dongdong.backend.services;

import com.dongdong.backend.entity.*;
import com.dongdong.backend.repository.GroupApplyRepository;
import com.dongdong.backend.repository.GroupRepository;
import com.dongdong.backend.repository.UserGroupRepository;
import com.dongdong.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    UserGroupRepository userGroupRepository;
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    GroupApplyRepository groupApplyRepository;
    @Autowired
    UserRepository userRepository;

    @Override
    public List<GroupVO> show(Long userId) {
        List<UserGroup> userGroups = userGroupRepository.findByUserId(userId);
        List<Long> ids = new ArrayList<>();
        for (UserGroup userGroup : userGroups) {
            ids.add(userGroup.getGroupId());
        }
        List<GroupVO> results = new ArrayList<>();
        List<Group> groups = groupRepository.findByGroupIdInOrderByGroupId(ids);

        for (Group group : groups) {
            results.add(new GroupVO(group));
        }
        return results;
    }

    @Override
    public List<GroupSearchVO> search(Long userId, String searchKey) {
        List<Group> groups = groupRepository.findByGroupNameLikeOrderByGroupId("%" + searchKey + "%");
        List<GroupSearchVO> results = new ArrayList<>();
        for (Group group : groups) {
            GroupSearchVO groupSearchVO = new GroupSearchVO();
            groupSearchVO.setGroupId(group.getGroupId());
            groupSearchVO.setGroupName(group.getGroupName());
            groupSearchVO.setIn(userGroupRepository.existsByUserIdAndGroupId(userId, group.getGroupId()));
            results.add(groupSearchVO);
        }
        return results;
    }

    @Override
    public void join(Long userId, Long groupId, String reason) {
        GroupApply groupApply = new GroupApply();
        groupApply.setGroupId(groupId);
        groupApply.setReason(reason);
        groupApply.setState(Long.valueOf(0));
        groupApply.setUserId(userId);
        groupApplyRepository.save(groupApply);
    }

    @Override
    public List<GroupRequestVO> showRequest(Long userId) {
        List<Group> groups = groupRepository.findByMasterIdOrderByGroupId(userId);
        List<Long> ids = new ArrayList<>();
        for (Group group : groups) {
            ids.add(group.getGroupId());
        }
        List<GroupRequestVO> results = new ArrayList<>();
        List<GroupApply> groupApplies = groupApplyRepository.findByGroupIdInOrderByGroupId(ids);
        for (GroupApply groupApply : groupApplies) {
            GroupRequestVO groupRequestVO = new GroupRequestVO();
            groupRequestVO.setGroupRequestId(groupApply.getApplyId());
            User user = userRepository.findByUserId(groupApply.getUserId()).get();
            UserInfoVO userInfoVO = new UserInfoVO();
            userInfoVO.setUserId(user.getUserId());
            userInfoVO.setUserName(user.getUserName());
            groupRequestVO.setUser(userInfoVO);
            groupRequestVO.setReason(groupApply.getReason());
            Group group = groupRepository.findByGroupId(groupApply.getGroupId()).get();
            GroupVO groupVO = new GroupVO();
            groupVO.setGroupId(group.getGroupId());
            groupVO.setGroupName(group.getGroupName());
            groupRequestVO.setGroup(groupVO);
            results.add(groupRequestVO);
        }
        return results;
    }

    @Override
    public GroupApply handleRequest(Long requestId, Long type) {
        GroupApply groupApply = groupApplyRepository.findByApplyId(requestId).get();
        if (type.equals(Long.valueOf(0))) {
            UserGroup userGroup = new UserGroup();
            userGroup.setUserId(groupApply.getUserId());
            userGroup.setGroupId(groupApply.getGroupId());
            userGroupRepository.save(userGroup);
        }
        groupApplyRepository.deleteByApplyId(requestId);
        return groupApply;
    }

    @Override
    public Long create(Long userId, String groupName) {
        Group group = new Group();
        group.setGroupName(groupName);
        group.setMasterId(userId);
        UserGroup userGroup = new UserGroup();
        group = groupRepository.save(group);
        Long id = group.getGroupId();
        userGroup.setGroupId(id);
        userGroup.setUserId(userId);
        userGroupRepository.save(userGroup);
        return id;
    }

    @Override
    public GroupInfoVO lookInfo(Long groupId) {
        Group group = groupRepository.findByGroupId(groupId).get();
        List<UserGroup> userGroups = userGroupRepository.findByGroupId(groupId);
        List<UserInfoVO> userInfoVOS = new ArrayList<>();
        GroupInfoVO result = new GroupInfoVO();
        result.setGroupId(groupId);
        result.setGroupName(group.getGroupName());
        for (UserGroup userGroup : userGroups) {
            User user = userRepository.findByUserId(userGroup.getUserId()).get();
            UserInfoVO userInfoVO = new UserInfoVO();
            userInfoVO.setUserName(user.getUserName());
            userInfoVO.setUserId(user.getUserId());
            userInfoVOS.add(userInfoVO);
        }
        result.setMembers(userInfoVOS);
        return result;
    }

    @Override
    public void quit(Long userId, Long groupId) {
        Group group = groupRepository.findByGroupId(groupId).get();
        if (group.getMasterId().equals(userId)) {
            userGroupRepository.deleteByGroupId(groupId);
            groupApplyRepository.deleteByGroupId(groupId);
            groupRepository.deleteByGroupId(groupId);
        } else {
            userGroupRepository.deleteByUserIdAndGroupId(userId, groupId);
        }
    }

    @Override
    public String addFriend(Long userId, Long groupId) {
        UserGroup  userGroup = new UserGroup();
        userGroup.setUserId(userId);
        userGroup.setGroupId(groupId);
        userGroupRepository.save(userGroup);

        // 返回群组的名称
        Group group = groupRepository.findByGroupId(groupId).get();
        return group.getGroupName();
    }

    @Override
    public void remove(Long userId, Long groupId) {
        userGroupRepository.deleteByUserIdAndGroupId(userId,groupId);
    }
}
