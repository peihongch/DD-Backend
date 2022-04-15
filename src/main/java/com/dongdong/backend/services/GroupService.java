package com.dongdong.backend.services;

import com.dongdong.backend.entity.GroupInfoVO;
import com.dongdong.backend.entity.GroupRequestVO;
import com.dongdong.backend.entity.GroupSearchVO;
import com.dongdong.backend.entity.GroupVO;

import java.util.List;

public interface GroupService {

    List<GroupVO> show(Long userId);

    List<GroupSearchVO> search(Long userId, String searchKey);

    void join(Long userId, Long groupId, String reason);

    List<GroupRequestVO> showRequest(Long userId);

    void handleRequest(Long requestId, Long type);

    Long create(Long userId, String groupName);

    GroupInfoVO lookInfo(Long groupId);

    void quit(Long userId,Long groupId);

    void addFriend(Long userId,Long groupId);

    void remove(Long userId,Long groupId);
}
