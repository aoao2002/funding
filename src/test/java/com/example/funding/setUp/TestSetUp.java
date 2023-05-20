package com.example.funding.setUp;

import cn.dev33.satoken.util.SaResult;
import com.example.funding.controller.GroupCtrl;
import com.example.funding.controller.UserCtrl;
import com.example.funding.service.Group.GroupInfo;
import com.example.funding.service.Group.GroupService;
import com.example.funding.service.User.RegisterInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;


/**
 * 这里准备基础信息
 * 人员：
 *  管理员 pre 123【但是这个就手插-或者到时候再用底层一点的方法】
 *  子管理员 aoao 123; her 123
 *  用户 y 123; x 123
 * 组：
 *  imed aoao, y
 *  bobo her, x
 * 基金：
 *  国自然
 *  国不自然
 * 申请
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestSetUp {
    @Resource
    UserCtrl userCtrl;

    @Resource
    GroupCtrl groupCtrl;

    @Resource
    GroupService groupService;

    @Test
    public void testAdmin(){
        RegisterInfo registerInfo = new RegisterInfo("1575720409@qq.com", "wta",
                "aoao", "1");
        SaResult res = userCtrl.register(registerInfo);
        boolean flag = false;
        if(SaResult.error("register error: user already exist").equals(res) || SaResult.ok("register success").equals(res)){
            flag = true;
        }
        Assertions.assertTrue(flag);

        registerInfo = new RegisterInfo("her@qq.com", "123",
                "her", "1");
        res = userCtrl.register(registerInfo);
        flag = false;
        if(SaResult.error("register error: user already exist").equals(res) || SaResult.ok("register success").equals(res)){
            flag = true;
        }
        Assertions.assertTrue(flag);
    }

    @Test
    public void testStaff(){
        RegisterInfo registerInfo = new RegisterInfo("y@qq.com", "123",
                "y", "0");
        userCtrl.register(registerInfo);
        registerInfo = new RegisterInfo("x@qq.com", "123", "x", "0");
        userCtrl.register(registerInfo);
//        test只要找得到
        SaResult user = userCtrl.getUserByMailAndIdentity("y@qq.com", "0");
        Assertions.assertNotNull(user);
        user = userCtrl.getUserByMailAndIdentity("x@qq.com", "0");
        Assertions.assertNotNull(user);
    }

    @Test
    public void testGroup(){
        String name = "imed";
        groupCtrl.createGroup(name);
        groupCtrl.assignManager(name, "aoao@qq.com");
        groupService.assignStaff(name, "y@qq.com");
        name = "bobo";
        groupCtrl.createGroup(name);
        groupCtrl.assignManager(name, "her@qq.com");
        groupService.assignStaff(name, "x@qq.com");

        List<GroupInfo> groups = groupService.getAllGroups();
        Optional<GroupInfo> groupInfo = groups.stream().filter(s->s.getGroupName().equals("imed")).findFirst();
        if (groupInfo.isPresent()){
//          TODO 好像没把小组成员的身份信息放进来
            Assertions.assertTrue(groupInfo.get().getMemberNames().contains("aoao"));
            Assertions.assertTrue(groupInfo.get().getMemberNames().contains("y"));
        }else{
            Assertions.fail();
        }
        groupInfo = groups.stream().filter(s->s.getGroupName().equals("bobo")).findFirst();
        if (groupInfo.isPresent()){
//            好像没把小组成员的身份信息放进来
            Assertions.assertTrue(groupInfo.get().getMemberNames().contains("her"));
            Assertions.assertTrue(groupInfo.get().getMemberNames().contains("x"));
        }else{
            Assertions.fail();
        }

    }
}
