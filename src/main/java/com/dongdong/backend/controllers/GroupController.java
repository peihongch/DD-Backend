package com.dongdong.backend.controllers;


import com.dongdong.backend.entity.*;
import com.dongdong.backend.services.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/group")
public class GroupController {

    @Autowired
    GroupService groupService;

    @GetMapping("/list")
    public Response show(@RequestParam Long userId){
        List<GroupVO> result=groupService.show(userId);
        if(result==null){
            return Response.error("error");
        }
        return Response.succeed(result);
    }

    @PostMapping("/search")
    public Response search(@RequestParam Long userId, @RequestParam String searchKey){
        List<GroupSearchVO> result=groupService.search(userId,searchKey);
        if(result==null){
            return Response.error("error");
        }
        return Response.succeed(result);
    }

    @PostMapping("/request/join")
    public Response join(@RequestParam Long userId,@RequestParam Long groupId, @RequestParam String reason){
        groupService.join(userId,groupId,reason);
        return Response.succeed(null);
    }

    @GetMapping("/request/list")
    public Response showRequests(@RequestParam Long userId){
        List<GroupRequestVO> result=groupService.showRequest(userId);
        if(result==null){
            return Response.error("error");
        }
        return Response.succeed(result);
    }

    @PostMapping("/request/handle")
    public Response handle(@RequestParam Long groupRequestId,@RequestParam Long type){
        groupService.handleRequest(groupRequestId,type);
        return  Response.succeed(null);
    }

    @PostMapping("/create")
    public Response create(@RequestParam Long userId,@RequestParam String groupName){
        Long id=groupService.create(userId,groupName);
        if(id==null){
            return Response.error("error");
        }
        return Response.succeed(id);
    }

    @GetMapping("/info")
    public Response showInfo(@RequestParam Long groupId){
        GroupInfoVO result=groupService.lookInfo(groupId);
        if(result==null){
            return Response.error("error");
        }
        return Response.succeed(result);
    }

    @PostMapping("/quit")
    public Response quit(@RequestParam Long userId,@RequestParam Long groupId){
        groupService.quit(userId,groupId);
        return Response.succeed(null);
    }
}
