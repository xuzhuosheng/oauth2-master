package com.gdth.jwtmaster.controller;


import com.alibaba.fastjson.JSONObject;
import com.gdth.jwtmaster.common.annotation.JwtIgnore;
import com.gdth.jwtmaster.common.interceptor.JwtInterceptor;
import com.gdth.jwtmaster.common.response.Result;
import com.gdth.jwtmaster.config.Audience;
import com.gdth.jwtmaster.util.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Slf4j
@RestController
public class AdminUserController {
    @Autowired
    private Audience audience;
    private static Logger logger = LoggerFactory.getLogger(AdminUserController.class);

    @PostMapping ("/login")
    @JwtIgnore  //有这个注解，忽略拦截
    public Result adminLogin(HttpServletResponse response, String username, String password) {
        // 这里模拟测试, 默认登录成功，返回用户ID和角色信息
        String userId = UUID.randomUUID().toString();
        String role = "admin";

        // 创建token
        String token = JwtTokenUtil.createJWT(userId, username, role, audience);
        logger.info("### 登录成功, token={} ###", token);

        // 将token放在响应头
        response.setHeader(JwtTokenUtil.AUTH_HEADER_KEY, JwtTokenUtil.TOKEN_PREFIX + token);
        // 将token响应给客户端
        JSONObject result = new JSONObject();
        result.put("token", token);
        return Result.SUCCESS(result);
    }

    @GetMapping ("/users")
    public Result userList() {
        logger.info("### 查询所有用户列表 ###");
        return Result.SUCCESS();
    }


    @PostMapping("/test")
    public String Test() {
        return "ssss";
    }


}
