package com.dongdong.backend.controllers;

import com.dongdong.backend.entity.Response;
import com.dongdong.backend.entity.User;
import com.dongdong.backend.entity.UserVo;
import com.dongdong.backend.services.SessionService;
import com.dongdong.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
     *
     * @param email
     * @param password
     * @return
     */
    @PostMapping("/register-email")
    public Response registerByEmail(@RequestParam(name = "email") String email,
                                    @RequestParam(name = "password") String password,
                                    @RequestParam(name = "userName") String userName) {

        Long userId = userService.signInByEmail(email, password, userName);
        sessionService.register(userId.toString(), false);
        return Response.succeed(userId);
    }

    /**
     * 使用电话号码注册
     *
     * @param phone
     * @param password
     * @return
     */
    @PostMapping("/register-phone")
    public Response registerByPhone(@RequestParam(name = "phone") String phone,
                                    @RequestParam(name = "password") String password,
                                    @RequestParam(name = "userName") String userName) {

        Long userId = userService.signInByPhone(phone, password, userName);
        sessionService.register(userId.toString(), false);
        return Response.succeed(userId);
    }

    /**
     * 登录
     *
     * @param userId
     * @param password
     * @param httpSession
     * @return
     */
    @PostMapping("/login")
    public Response login(@RequestParam(name = "userId") String userId,
                          @RequestParam(name = "password") String password, HttpSession httpSession) {
        User user = userService.login(userId, password);
        if (user == null) {
            return Response.error("用户名或密码错误，登录失败。");
        } else {
            httpSession.setAttribute("user", user);
            return Response.succeed(user);
        }
    }

    /**
     * 获取用户详细信息
     *
     * @param userId
     * @return
     */
    @GetMapping("/get-information")
    public Response getInformation(@RequestParam(name = "userId") String userId, HttpSession httpSession) {
        User u = (User) httpSession.getAttribute("user");
        UserVo user = userService.getUser(userId, u.getUserId());
        if (user == null) {
            return Response.error("该用户不存在或获取失败");
        } else {
            return Response.succeed(user);
        }
    }

    /**
     * 退出登录
     *
     * @param httpSession
     * @return
     */
    @GetMapping("/logout")
    public Response logout(HttpSession httpSession) {
        httpSession.removeAttribute("user");
        return Response.succeed("成功退出登录。");
    }

    /**
     * 使用id查找用户
     *
     * @param userId
     * @return
     */
    @GetMapping("/search-id")
    public Response searchById(@RequestParam(name = "userId") String userId, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        UserVo userVo = userService.getUser(userId, user.getUserId());
        if (userVo == null) {
            return Response.error("该用户不存在或获取失败");
        } else {
            return Response.succeed(userVo);
        }
    }

    /**
     * 根据用户名查找用户
     *
     * @param userName
     * @return
     */
    @GetMapping("/search-name")
    public Response searchByName(@RequestParam(name = "userName") String userName, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        List<UserVo> users = userService.searchUser(userName, user.getUserId());
        if (users == null) {
            return Response.error("该用户不存在或获取失败");
        } else {
            return Response.succeed(users);
        }
    }

    @PostMapping("/set-information")
    public Response setInformation(@RequestParam(name = "userId") String userId,
                                   @RequestParam(name = "userName") String userName,
                                   @RequestParam(name = "email") String email,
                                   @RequestParam(name = "phone") String phone,
                                   @RequestParam(name = "age") int age,
                                   @RequestParam(name = "gender") int gender) {
        boolean res = userService.setUser(userId, userName, email, phone, age, gender);
        if (res) {
            return Response.succeed("修改成功。");
        } else {
            return Response.error("修改失败。");
        }
    }


}
