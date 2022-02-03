package com.dongdong.backend.controllers;

import com.dongdong.backend.entity.Response;
import com.dongdong.backend.entity.User;
import com.dongdong.backend.services.SessionService;
import com.dongdong.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private SessionService sessionService;

    /**
     * 使用邮箱注册
     * @param email
     * @param password
     * @return
     */
    @GetMapping("/register-email")
    public Response registerByEmail(@RequestParam(name = "email") String email,
                           @RequestParam(name = "password") String password){

        Long userId=userService.signInByEmail(email,password);
        sessionService.register(userId.toString());
        return Response.succeed(userId);
    }

    /**
     * 使用电话号码注册
     * @param phone
     * @param password
     * @return
     */
    @GetMapping("/register-phone")
    public Response registerByPhone(@RequestParam(name = "phone") String phone,
                                  @RequestParam(name = "password") String password){

        Long userId=userService.signInByPhone(phone,password);
        sessionService.register(userId.toString());
        return Response.succeed(userId);
    }

    /**
     * 登录
     * @param userId
     * @param password
     * @param httpSession
     * @return
     */
    @PostMapping("/login")
    public Response login(@RequestParam(name = "userId") String userId,
                          @RequestParam(name = "password") String password, HttpSession httpSession){
        User user=userService.login(userId,password);
        if(user==null){
            return Response.error("用户名或密码错误，登录失败。");
        }else{
            httpSession.setAttribute("user",user);
            return Response.succeed(user);
        }
    }

    /**
     * 获取用户详细信息
     * @param userId
     * @return
     */
    @GetMapping("/get-information")
    public Response getInformation(@RequestParam(name = "userId") String userId){
        User user=userService.getUser(userId);
        if(user==null){
            return Response.error("该用户不存在或获取失败");
        }else{
            return Response.succeed(user);
        }
    }

    /**
     * 退出登录
     * @param httpSession
     * @return
     */
    @GetMapping("/logout")
    public Response logout(HttpSession httpSession){
        httpSession.removeAttribute("user");
        return Response.succeed("成功退出登录。");
    }

    /**
     * 使用id查找用户
     * @param userId
     * @return
     */
    @GetMapping("/search-id")
    public Response searchById(@RequestParam(name = "userId") String userId){
        User user=userService.getUser(userId);
        if(user==null){
            return Response.error("该用户不存在或获取失败");
        }else{
            return Response.succeed(user);
        }
    }

    /**
     * 根据用户名查找用户
     * @param userName
     * @return
     */
    @GetMapping("/search-name")
    public Response searchByName(@RequestParam(name = "userName") String userName){
        List<User> user=userService.searchUser(userName);
        if(user==null){
            return Response.error("该用户不存在或获取失败");
        }else{
            return Response.succeed(user);
        }
    }



}
