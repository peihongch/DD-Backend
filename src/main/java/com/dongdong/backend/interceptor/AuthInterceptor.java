package com.dongdong.backend.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        String uri = request.getRequestURI();
        if (uri.endsWith("js")||uri.endsWith("css")||uri.endsWith("jpg")||uri.endsWith("svg")||uri.endsWith("jpg")||uri.endsWith("png")){
            return true ;
        }
        HttpSession session = request.getSession();
        // 获取用户信息，如果没有用户信息直接返回提示信息
        Object userInfo = session.getAttribute("loginUser");
        if (userInfo == null) {
            request.setAttribute("msg","请先登录！");
            return false;
        }

        return true;
    }
}
