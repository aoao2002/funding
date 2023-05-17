package com.example.funding.controller;

import cn.dev33.satoken.util.SaResult;
import com.example.funding.service.User.EmailAndPwd;
import com.example.funding.service.User.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

/*
准备基础人员，组别，基金以及申请

 */

@SpringBootTest
public class TestGroup {

    @Autowired
    UserCtrl userCtrl;
    @Autowired
    UserService userService;
    @Autowired
    GroupCtrl groupCtrl;
    @Test
    public void testCreateGroup() {
        String groupName = "test77";
        //登陆一个Staff
        String email = "123@qq.com";String password = "123";
        String identity = "0";
        SaResult res = userService.LoginMail(email, password, identity);
        assertEquals(200,res.getCode());

        //创建一个组
        res = groupCtrl.createGroup(groupName);
        assertEquals(500, res.getCode()); //因为用url限制的，方法内部没判断

        //登出
        res = userCtrl.Logout();
        assertEquals(200,res.getCode());

        //登陆一个Manager
        //登陆一个Staff
        email = "123@qq.com"; password = "123";
        identity = "1";
        res = userService.LoginMail(email, password, identity);
        assertEquals(200,res.getCode());

        //创建一个组
        res = groupCtrl.createGroup(groupName);
        assertEquals( 500,res.getCode());

        //登出
        res = userCtrl.Logout();
        assertEquals( 200,res.getCode());

        //登陆一个President
        email = "admin@qq.com"; password = "admin";
        identity = "2";
        res = userService.LoginMail(email, password, identity);
        assertEquals( 200,res.getCode());

        //创建一个组
        res = groupCtrl.createGroup(groupName);
        assertEquals(200,res.getCode());

        //登出
        res = userCtrl.Logout();
        assertEquals(200,res.getCode());
    }

}
