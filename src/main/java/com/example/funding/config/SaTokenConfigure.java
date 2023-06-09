package com.example.funding.config;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import com.example.funding.FundingApplication;
import com.example.funding.Util.Exception.EditException;
import com.example.funding.service.Expenditure.ExpenditureService;
import com.example.funding.service.Group.GroupService;
import com.example.funding.service.User.UserService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.slf4j.Logger;

@Configuration
public class SaTokenConfigure implements WebMvcConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(SaTokenConfigure.class);

    @Autowired
    private ExpenditureService expenditureService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private UserService userService;


    /***
     * 注册 Sa-Token 拦截器，打开注解式鉴权功能
     * 1. 在service实现权限鉴定
     * 2. 在下方实现鉴权函数
     * 3. 在addInterceptors注册鉴权函数
      */

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
                    // 申请权限

                    // 经费权限(修改只能manager和president)
                    SaRouter.match("/expenditure/edit/**")
                            .check(r -> checkManagerAndPresident());
//                    SaRouter.match("/expenditure/edit/check/**")
//                                    .check(r->checkExpendStatus());

                    // group权限
                    SaRouter.match("/group/president/**")
                            .check(r -> checkPresident());

                    SaRouter.match("/group/edit/**")
                            .check(r -> checkManagerAndPresident());

                    SaRouter.match("/user/edit/getAllManagers")
                            .check(r -> checkPresident());

                }))
                .addPathPatterns("/**")
                // 以下api不用进行权限认证
                .excludePathPatterns("/swagger-ui.html#/")
                .excludePathPatterns("/user/LoginEmail")
                .excludePathPatterns("/user/register")
                .excludePathPatterns("/user/isLogin")
                .excludePathPatterns("/user/sendEmail")
                .excludePathPatterns("/user/checkCode")
        ;
    }
    /***
     * 鉴权函数：
     * 1. 搜集需要信息(前端传入信息)
     * 2. 传入Service层完成鉴权
     * 3. 未通过抛出异常，将在 funding/Util/Handler/GlobalExceptionHandler.java被统一处理
     */

    public void checkPresident(){
        if (!userService.checkPresident())
            throw new EditException("The user is not the president");
    }

    public void checkManager(){
        if (!userService.checkManager())
            throw new EditException("The user is not the manager");
    }

    public void checkStaff(){
        if (!userService.checkStaff())
            throw new EditException("The user is not the staff");
    }

    public void checkManagerAndPresident(){
        if (!userService.checkPresident() && !userService.checkManager())
            throw new EditException("The user is not the manager or president");
    }

    public void checkExpendStatus(){

    }

    public void checkGroupEditPower(){
        if (!groupService.checkGroupEditPower(SaHolder.getRequest().getParam("applyId"),
                SaHolder.getRequest().getParam("GroupName"),
                StpUtil.getLoginIdAsString()))

            throw new EditException("No access to edit this group");
    }

}
