package com.example.funding.controller;

import cn.dev33.satoken.util.SaResult;
import com.example.funding.bean.User;
import com.example.funding.dao.UserDao;
import com.example.funding.service.User.RegisterInfo;
import com.example.funding.service.User.UserInfo;
import com.example.funding.service.User.UserService;
import com.google.common.base.Strings;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 建立基本信息
 * 1. 将所有table drop
 * 2. 管理员：
 * TODO 这里加入没有管是否已存在的情况，按理应该避免
 */
@SpringBootTest
public class TestUsers {

    @Autowired
    UserCtrl userCtrl;
    @Autowired
    UserService userService;
    @Autowired
    private UserDao userDao;

    @Test
    public void registerPresident(){
        RegisterInfo registerInfo = new RegisterInfo("bill@qq.com", "123",
                "bill", "2");
        SaResult res = userCtrl.register(registerInfo);
        assertEquals(res, SaResult.error("could not register as president"));
    }

    @Test
    public void registerManagers(){
        String name = RandomStringUtils.randomAlphanumeric(3);
        RegisterInfo registerInfo = new RegisterInfo(name+"@qq.com", "123",
                name, "1");
        SaResult res = userCtrl.register(registerInfo);
        assertEquals(res, SaResult.ok("register success"));
    }

    @Test
    public void registerStaff(){
        // random generate string
        String name = RandomStringUtils.randomAlphanumeric(3);
        RegisterInfo registerInfo = new RegisterInfo(name+"@qq.com", "123",
                name, "0");
        SaResult res = userCtrl.register(registerInfo);
        assertEquals(res, SaResult.ok("register success"));
        int nameInt = RandomStringUtils.randomAlphanumeric(3).hashCode();
        String name2 = String.valueOf(nameInt);
        RegisterInfo registerInfo3 = new RegisterInfo(name2+"@qq.com", "123",
                name2, "0");
        SaResult res3 = userCtrl.register(registerInfo3);
        assertEquals(res3, SaResult.ok("register success"));
    }

    @Test
    public void logInAndLogOut(){
        //登陆一个Staff(成功)
        String email = "123@qq.com";String password = "123";
        String identity = "0";
        SaResult res = userService.LoginMail(email, password, identity);
        assertEquals(200,res.getCode());
        //登出
        res = userCtrl.Logout();
        assertEquals(200,res.getCode());
        //登陆一个Staff(失败)
        email = "xxx@qq.com"; password = "123";
        identity = "0";
        res = userService.LoginMail(email, password, identity);
        assertEquals(500,res.getCode());
        //登出
        res = userCtrl.Logout();
        assertEquals(200,res.getCode());
        //登陆一个Manager(成功)
        email = "123@qq.com"; password = "123";
        identity = "1";
        res = userService.LoginMail(email, password, identity);
        assertEquals(200,res.getCode());
        //登出
        res = userCtrl.Logout();
        assertEquals(200,res.getCode());
        //登陆一个Manager(失败)
        email = "bill@qq.com"; password = "abc";
        identity = "1";
        res = userService.LoginMail(email, password, identity);
        assertEquals(500,res.getCode());
        //登陆一个President(成功)
        email = "admin@qq.com"; password = "admin";
        identity = "2";
        res = userService.LoginMail(email, password, identity);
        assertEquals( 200,res.getCode());
        //登出
        res = userCtrl.Logout();
        assertEquals(200,res.getCode());
        //登陆一个President(失败)
        email = "aoao@qq.com"; password = "123";
        identity = "2";
        res = userService.LoginMail(email, password, identity);
        assertEquals(500,res.getCode());
    }

    @Test
    public void getInfo(){
        //测试getPresidents
        SaResult res = userCtrl.getPresidents();
        List<UserInfo> presidents = new ArrayList<>();

        //
    }
}
