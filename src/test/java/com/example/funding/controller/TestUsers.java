package com.example.funding.controller;

import cn.dev33.satoken.util.SaResult;
import com.example.funding.service.User.RegisterInfo;
import com.google.common.base.Strings;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;


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

    @Test
    public void addPresident(){
        RegisterInfo registerInfo = new RegisterInfo("bill@qq.com", "123",
                "bill", "2");
        SaResult res = userCtrl.register(registerInfo);
        assertEquals(res, SaResult.ok("register success"));
    }

    @Test
    public void addManagers(){
        RegisterInfo registerInfo = new RegisterInfo("aoao@qq.com", "123",
                "aoao", "1");
        SaResult res = userCtrl.register(registerInfo);
        assertEquals(res, SaResult.ok("register success"));
        RegisterInfo registerInfo2 = new RegisterInfo("her@qq.com", "123",
                "her", "1");
        SaResult res2 = userCtrl.register(registerInfo2);
        assertEquals(res2, SaResult.ok("register success"));
    }

    @Test
    public  void addStaff(){
        RegisterInfo registerInfo = new RegisterInfo("y@qq.com", "123",
                "y", "0");
        SaResult res = userCtrl.register(registerInfo);
        assertEquals(res, SaResult.ok("register success"));
        RegisterInfo registerInfo2 = new RegisterInfo("x@qq.com", "123",
                "x", "0");
        SaResult res2 = userCtrl.register(registerInfo2);
        assertEquals(res2, SaResult.ok("register success"));
        RegisterInfo registerInfo3 = new RegisterInfo("123@qq.com", "123",
                "123", "0");
        SaResult res3 = userCtrl.register(registerInfo3);
        assertEquals(res3, SaResult.ok("register success"));
    }

}
