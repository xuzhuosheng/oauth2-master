package com.gdth.proauthorization.config;

import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

import java.util.Date;

/**
 * spring security配置
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity (prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class Oauth2WebSecurityConfigurer extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        System.out.println("Oauth2WebSecurityConfigurer" + new Date());
        http.authorizeRequests()
                .anyRequest().authenticated()
//                放行登录和身份验证接口
                .antMatchers("/login").permitAll()
                .antMatchers("/oauth/**").permitAll()
                .and()
                .formLogin().permitAll()
                .and()
//                要是没有登录就抛出401异常
//                .exceptionHandling()
//                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
//                .and()
                .csrf().disable();
    }
}
