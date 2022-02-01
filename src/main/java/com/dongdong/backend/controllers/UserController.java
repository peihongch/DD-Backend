package com.dongdong.backend.controllers;

import com.dongdong.backend.entity.Response;
import com.dongdong.backend.entity.User;
import com.dongdong.backend.services.SessionService;
import com.dongdong.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

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

    @GetMapping("/get-information")
    public Response getInformation(@RequestParam(name = "userId") String userId){
        User user=userService.getUser(userId);
        if(user==null){
            return Response.error("该用户不存在或获取失败");
        }else{
            return Response.succeed(user);
        }
    }



}
