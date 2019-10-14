package com.gdth.proauthorization.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;

import java.util.Date;

@Configuration
@EnableAuthorizationServer  //授权服务器
public class Oauth2AuthorizationServerConfigurer extends AuthorizationServerConfigurerAdapter {

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        System.out.println("Oauth2AuthorizationServerConfigurer:" + new Date());
        clients.inMemory()
                .withClient("admin")
                .secret(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("123456"))
                .authorizedGrantTypes("authorization_code", "refresh_token") //授权模式为授权码模式
                .scopes("all")
                .redirectUris("http://localhost:5003/user/code") //用户访问服务自定义的接口，用来接收授权码code
                .autoApprove(true)
                .accessTokenValiditySeconds(36000);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {

        security.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()").allowFormAuthenticationForClients();
    }
}
