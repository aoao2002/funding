package com.example.funding.controller;

import cn.dev33.satoken.util.SaResult;
import com.example.funding.service.Group.GroupService;
import com.example.funding.service.User.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

/*
准备基础人员，组别，基金以及申请

 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestGroup {

    @Resource
    UserCtrl userCtrl;
    @Resource
    UserService userService;
    @Resource
    GroupCtrl groupCtrl;

    @Resource
    GroupService groupService;


    @Test
    public void testGroup() {
        SaResult res;
        String groupName = RandomStringUtils.randomAlphanumeric(3);
        //创建一个组
        res = groupCtrl.createGroup(groupName);
        assertEquals(200, res.getCode()); //因为用url限制的，方法内部没判断

        //申请加入一个组
        res= groupService.applyGroup(groupName,"test",2L);
        assertEquals(200, res.getCode());
        res = groupService.applyGroup(groupName +"daShaZi","test",1L);
        assertEquals(500, res.getCode());
        res = groupService.applyGroup(groupName,"test",1000000L);
        assertEquals(500, res.getCode());
        //同意加入

    }
}
