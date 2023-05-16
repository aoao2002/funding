package com.example.funding.controller;

import cn.dev33.satoken.util.SaResult;
import com.example.funding.bean.User;
import com.example.funding.dao.UserDao;
import com.example.funding.service.User.RegisterInfo;
import com.google.common.base.Strings;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;


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
    private UserDao userDao;

    @Test
    public void addPresident(){
        RegisterInfo registerInfo = new RegisterInfo("bill@qq.com", "123",
                "bill", "2");
        SaResult res = userCtrl.register(registerInfo);
        assertEquals(res, SaResult.error("could not register as president"));
    }

    @Test
    public void addManagers(){
        String name = RandomStringUtils.randomAlphanumeric(3);
        RegisterInfo registerInfo = new RegisterInfo(name+"@qq.com", "123",
                name, "1");
        SaResult res = userCtrl.register(registerInfo);
        assertEquals(res, SaResult.ok("register success"));
    }

    @Test
    public void addStaff(){
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

}
