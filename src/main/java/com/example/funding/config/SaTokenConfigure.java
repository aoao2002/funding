package com.example.funding.config;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import com.example.funding.FundingApplication;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.slf4j.Logger;

@Configuration
public class SaTokenConfigure implements WebMvcConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(SaTokenConfigure.class);


    // 注册 Sa-Token 拦截器，打开注解式鉴权功能
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // 注册 Sa-Token 拦截器，校验规则为 StpUtil.checkLogin() 登录校验。
        // 基于路由的权限认证，适合只有少数接口开放（dologin）,其他全需要登录
        registry.addInterceptor(new SaInterceptor(handle ->{
                    SaRouter.match("/**")
                            .check(r -> {
                                FundingApplication.getLogger().info(SaHolder.getRequest().getUrl());
                                StpUtil.checkLogin();
                            });
                }))
                .addPathPatterns("/**")
                .excludePathPatterns("/swagger-ui.html#/")
                .excludePathPatterns("/user/LoginEmail")
                .excludePathPatterns("/user/register")
                .excludePathPatterns("/user/isLogin")
        ;
    }



}
