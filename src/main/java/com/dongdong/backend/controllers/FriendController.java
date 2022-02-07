package com.dongdong.backend.controllers;

import com.dongdong.backend.entity.Friend;
import com.dongdong.backend.entity.FriendApply;
import com.dongdong.backend.entity.Response;
import com.dongdong.backend.services.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/friend")
public class FriendController {

    @Autowired
    private FriendService friendService;

    /**
     * 好友申请
     *
     * @param userId
     * @param friendId
     * @param msg
     * @return
     */
    @GetMapping("/apply")
    public Response apply(@RequestParam(name = "userId") String userId, @RequestParam(name = "friendId") String friendId, @RequestParam(name = "msg") String msg) {
        boolean res = friendService.apply(userId, friendId, msg);
        if (res) {
            return Response.succeed("申请成功。");
        } else {
            return Response.error("申请失败。");
        }
    }

    /**
     * 接受好友申请
     *
     * @param userId
     * @param friendId
     * @return
     */
    @GetMapping("/accept")
    public Response accept(@RequestParam(name = "userId") String userId, @RequestParam(name = "friendId") String friendId) {
        boolean res = friendService.accept(userId, friendId);
        if (res) {
            return Response.succeed("接受申请。");
        } else {
            return Response.error("接受失败。");
        }
    }

    /**
     * 拒绝好友申请
     *
     * @param userId
     * @param friendId
     * @return
     */
    @GetMapping("/refuse")
    public Response refuse(@RequestParam(name = "userId") String userId, @RequestParam(name = "friendId") String friendId) {
        boolean res = friendService.refuse(userId, friendId);
        if (res) {
            return Response.succeed("拒绝申请。");
        } else {
            return Response.error("拒绝失败。");
        }
    }

    /**
     * 查看好友列表
     *
     * @param userId
     * @return
     */
    @GetMapping("/get-friend-list")
    public Response getFriendList(@RequestParam(name = "userId") String userId) {
        List<Friend> friends = friendService.getFriendList(userId);
        if (friends != null) {
            return Response.succeed(friends);
        } else {
            return Response.error("获取失败。");
        }
    }

    /**
     * 删除好友
     *
     * @param userId
     * @param friendId
     * @return
     */
    @GetMapping("/delete")
    public Response delete(@RequestParam(name = "userId") String userId, @RequestParam(name = "friendId") String friendId) {
        boolean res = friendService.delete(userId, friendId);
        if (res) {
            return Response.succeed("拒绝申请。");
        } else {
            return Response.error("拒绝失败。");
        }
    }

    /**
     * 拉黑好友
     *
     * @param userId
     * @param friendId
     * @return
     */
    @GetMapping("/black")
    public Response blackList(@RequestParam(name = "userId") String userId, @RequestParam(name = "friendId") String friendId) {
        boolean res = friendService.black(userId, friendId);
        if (res) {
            return Response.succeed("拉黑成功。");
        } else {
            return Response.error("拉黑失败。");
        }
    }

    /**
     * 解除拉黑
     *
     * @param userId
     * @param friendId
     * @return
     */
    @GetMapping("/deblack")
    public Response deblackList(@RequestParam(name = "userId") String userId, @RequestParam(name = "friendId") String friendId) {
        boolean res = friendService.deblack(userId, friendId);
        if (res) {
            return Response.succeed("解除拉黑成功。");
        } else {
            return Response.error("解除拉黑失败。");
        }
    }

    @GetMapping("/get-apply-list")
    public Response getApplyList(@RequestParam(name = "userId") String userId) {
        List<FriendApply> res = friendService.getApplyList(userId);
        if (res != null) {
            return Response.succeed(res);
        } else {
            return Response.error("获取失败。");
        }
    }

}
