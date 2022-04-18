package com.dongdong.backend.controllers;

import com.dongdong.backend.entity.Friend;
import com.dongdong.backend.entity.FriendApplyVo;
import com.dongdong.backend.entity.Message;
import com.dongdong.backend.entity.Response;
import com.dongdong.backend.services.FriendService;
import com.dongdong.backend.services.SessionService;
import com.dongdong.backend.utils.TimeUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
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

    @Autowired
    private SessionService sessionService;

    private static final String MASTER_ID="9999";

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
            try {
                var message=new Message(MASTER_ID,userId,0,Message.TYPE_TEXT, TimeUtils.currentTimestamp(),"Your invitation has been sent successfully");
                sessionService.send(message.receiver(),message.timestamp(),Message.marshal(message));
                message=new Message(MASTER_ID,friendId,0,Message.TYPE_TEXT, TimeUtils.currentTimestamp(),"You have received an invitation");
                sessionService.send(message.receiver(),message.timestamp(),Message.marshal(message));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return Response.succeed("applied");
        } else {
            return Response.error("failed");
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
            Message message=new Message(MASTER_ID,friendId,0,Message.TYPE_TEXT, TimeUtils.currentTimestamp(),"Your invitation has been accepted");
            try {
                sessionService.send(message.receiver(),message.timestamp(),Message.marshal(message));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return Response.succeed("accepted");
        } else {
            return Response.error("failed");
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
            Message message=new Message(MASTER_ID,friendId,0,Message.TYPE_TEXT, TimeUtils.currentTimestamp(),"Your invitation has been declined");
            try {
                sessionService.send(message.receiver(),message.timestamp(),Message.marshal(message));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return Response.succeed("declined");
        } else {
            return Response.error("failed");
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
            return Response.error("failed");
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
            return Response.succeed("deleted");
        } else {
            return Response.error("failed");
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
            return Response.succeed("succeed");
        } else {
            return Response.error("failed");
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
            return Response.succeed("succeed");
        } else {
            return Response.error("failed");
        }
    }

    @GetMapping("/get-apply-list")
    public Response getApplyList(@RequestParam(name = "userId") String userId) {
        List<FriendApplyVo> res = friendService.getApplyList(userId);
        if (res != null) {
            return Response.succeed(res);
        } else {
            return Response.error("failed");
        }
    }

}
