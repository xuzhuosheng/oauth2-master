package com.gdth.conuserservice.controller;

import com.google.gson.JsonParser;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

import java.net.URI;


@Slf4j
@RestController
@RequestMapping ("/user")
public class UserController {


    @Autowired
    RestTemplate restTemplate;


    @GetMapping ("/login")
    public ResponseEntity login() throws Exception {
        System.out.println("login请求开始");
        //spring-cloud-oauth2 登录验证的地址
        URI oauthUrl = new URIBuilder("http://localhost:5002/oauth/authorize")
                .addParameter("client_id", "admin")
//                .addParameter("client_secret", "123456")
                .addParameter("response_type", "code")
                .addParameter("redirect_uri", "http://localhost:5003/user/code")
                .build();

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

    //http 配置信息
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(5000)// 设置连接超时时间(单位毫秒)
                .setConnectionRequestTimeout(5000) // 设置请求超时时间(单位毫秒)
                .setSocketTimeout(5000)// socket读写超时时间(单位毫秒)
                .setRedirectsEnabled(false)// 设置是否允许重定向(默认为true)
                .build();

        HttpGet oauthHttpGet = new HttpGet(oauthUrl);
        oauthHttpGet.setConfig(requestConfig); //将上面的配置信息 运用到这个Get请求里

    //响应模型 由客户端执行(发送)Get请求
        CloseableHttpResponse response = httpClient.execute(oauthHttpGet);

    //返回的是重定向302
        if (response.getStatusLine().getStatusCode() == HttpStatus.FOUND.value()) {
            //获取Set-Cookie成为登录页面的cookie
            String setCookie = response.getFirstHeader("Set-Cookie").getValue();
            String cookie = setCookie.substring(0, setCookie.indexOf(";"));

            //登录页面获取token
            MultiValueMap<String, String> loginParams = new LinkedMultiValueMap<>();
            loginParams.add("username", "admin");
            loginParams.add("password", "123456");

            //添加cookie
            HttpHeaders loginHeader = new HttpHeaders();
            loginHeader.set("Cookie", cookie);


            HttpEntity<MultiValueMap<String, String>> loginEntity = new HttpEntity<>(loginParams, loginHeader);

            String loginUrl = "http://localhost:5002/login";


            ResponseEntity<String> loginResult = restTemplate.postForEntity(loginUrl, loginEntity, String.class);


            System.out.println("---- 登录请求结果：{} ----" + loginResult);

            return loginResult;

        }

        httpClient.close();
        response.close();
        return null;

    }


    public ResponseEntity code(@RequestParam ("code") String code) {

        System.out.println("---- 获取授权码：{} ----" + code);
        MultiValueMap<String, String> tokenParams = new LinkedMultiValueMap<>();
        tokenParams.add("grant_type", "authorization_code");
        tokenParams.add("code", code);
        tokenParams.add("client_id", "admin");
        tokenParams.add("client_secret", "123456");
        tokenParams.add("redirect_uri", "http://localhost:5003/user/code");
        tokenParams.add("scope", "all");

        HttpHeaders tokenHeader = new HttpHeaders();
        tokenHeader.set("Content-Type", "multipart/form-data");
        HttpEntity<MultiValueMap<String, String>> requestEntity =
                new HttpEntity<>(tokenParams, tokenHeader);

        ResponseEntity<String> tokenResult = restTemplate.postForEntity(
                "http://localhost:5002/oauth/token", requestEntity, String.class);


        System.out.println("---- 获取token结果：{} ----" + tokenResult);


        String token = new JsonParser().parse(tokenResult.getBody()).
                getAsJsonObject().get("access_token").getAsString();

        System.out.println("---- access_token：{} ----" + token);


        //访问资源服务，仅仅能用来验证登录效果
        HttpHeaders resourceHeader = new HttpHeaders();
        resourceHeader.set("Authorization", "Bearer " + token);

        ResponseEntity<String> resourceResult = restTemplate.exchange(
                "http://localhost:5004/getResource", HttpMethod.GET,
                new HttpEntity<String>(null, resourceHeader), String.class);

        System.out.println("获取资源的结果：{}" + resourceResult);

        return tokenResult;

    }

}
