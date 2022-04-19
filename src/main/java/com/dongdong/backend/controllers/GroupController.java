package com.dongdong.backend.controllers;


import com.dongdong.backend.entity.*;
import com.dongdong.backend.services.GroupService;
import com.dongdong.backend.services.SessionService;
import com.dongdong.backend.utils.TimeUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/group")
public class GroupController {

    @Autowired
    GroupService groupService;

    @Autowired
    private SessionService sessionService;

    private static final String MASTER_ID="9999";

    @GetMapping("/list")
    public Response show(@RequestParam Long userId) {
        List<GroupVO> result = groupService.show(userId);
        if (result == null) {
            return Response.error("error");
        }
        return Response.succeed(result);
    }

    @PostMapping("/search")
    public Response search(@RequestParam Long userId, @RequestParam String searchKey) {
        List<GroupSearchVO> result = groupService.search(userId, searchKey);
        if (result == null) {
            return Response.error("error");
        }
        return Response.succeed(result);
    }

    @PostMapping("/request/join")
    public Response join(@RequestParam Long userId, @RequestParam Long groupId, @RequestParam String reason) {
        groupService.join(userId, groupId, reason);
        return Response.succeed(null);
    }

    @GetMapping("/request/list")
    public Response showRequests(@RequestParam Long userId) {
        List<GroupRequestVO> result = groupService.showRequest(userId);
        if (result == null) {
            return Response.error("error");
        }
        return Response.succeed(result);
    }

    @PostMapping("/request/handle")
    public Response handle(@RequestParam Long groupRequestId, @RequestParam Long type) {
        GroupApply groupApply=groupService.handleRequest(groupRequestId, type);
        if (type.equals(Long.valueOf(0))){
            String topicName = sessionService.getTopicName(String.valueOf(groupApply.getGroupId()), true);
            sessionService.updateKafkaConsumer(String.valueOf(groupApply.getUserId()), topicName, Operation.ADD);
        }
        return Response.succeed(null);
    }

    @PostMapping("/create")
    public Response create(@RequestParam Long userId, @RequestParam String groupName) {
        Long id = groupService.create(userId, groupName);
        if (id == null) {
            return Response.error("error");
        }
        return Response.succeed(id);
    }

    @GetMapping("/info")
    public Response showInfo(@RequestParam Long groupId) {
        GroupInfoVO result = groupService.lookInfo(groupId);
        if (result == null) {
            return Response.error("error");
        }
        return Response.succeed(result);
    }

    @PostMapping("/quit")
    public Response quit(@RequestParam Long userId, @RequestParam Long groupId) {
        groupService.quit(userId, groupId);
        String topicName = sessionService.getTopicName(String.valueOf(groupId), true);
        sessionService.updateKafkaConsumer(String.valueOf(userId), topicName, Operation.DELETE);
        return Response.succeed(null);
    }

    @PostMapping("/invite")
    public Response invite(@RequestParam Long userId,@RequestParam Long groupId){
        String groupName=groupService.addFriend(userId,groupId);
        String msg="You have been invites to "+groupName+"(ID: "+groupId+")";
        try {
            var message=new Message(MASTER_ID,userId.toString(),0,Message.TYPE_TEXT, TimeUtils.currentTimestamp(),msg);
            sessionService.send(message.receiver(),message.timestamp(),Message.marshal(message));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        String topicName = sessionService.getTopicName(String.valueOf(groupId), true);
        sessionService.updateKafkaConsumer(String.valueOf(userId), topicName, Operation.ADD);
        return Response.succeed(null);
    }

    @PostMapping("/remove")
    public Response remove(@RequestParam Long userId, @RequestParam Long groupId){
        groupService.remove(userId,groupId);
        String topicName = sessionService.getTopicName(String.valueOf(groupId), true);
        sessionService.updateKafkaConsumer(String.valueOf(userId), topicName, Operation.DELETE);
        return Response.succeed(null);
    }
}
