package com.example.funding.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.example.funding.Util.Handler.InputChecker;
import com.example.funding.Util.Handler.ReturnHelper;
import com.example.funding.service.User.EmailAndPwd;
import com.example.funding.service.User.RegisterInfo;
import com.example.funding.service.User.UserInfo;
import com.example.funding.service.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import cn.dev33.satoken.util.SaResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@RestController
@RequestMapping("user/")
public class UserCtrl {
    private static final Logger logger = LoggerFactory.getLogger(UserCtrl.class);

    @Autowired
    private UserService userService;

    @RequestMapping(value ="register", method= RequestMethod.POST)
    public SaResult register(@RequestBody RegisterInfo registerInfo){
        return userService.addUser(registerInfo.getEmail(), registerInfo.getPwd(),
                registerInfo.getName(), registerInfo.getIdentity());
    }

    @RequestMapping(value ="logout", method= RequestMethod.POST)
    public SaResult Logout(){
        StpUtil.logout();
        return SaResult.ok("logout success");
    }

    @RequestMapping(value ="getUserByName", method= RequestMethod.GET)
    public SaResult getUserByName(String name){
        return ReturnHelper.returnObj(userService.getUserByName(name));
    }

    @RequestMapping(value ="getUserByMail", method= RequestMethod.GET)
    public SaResult getUserByMail(String mail){
        return ReturnHelper.returnObj(userService.getUserByMail(mail));
    }

    @RequestMapping(value ="isLogin", method= RequestMethod.GET)
    public SaResult isLogin() {
        return SaResult.ok("isLogin：" + StpUtil.isLogin());
    }

    @RequestMapping(value ="LoginEmail", method= RequestMethod.POST)
    @ResponseBody
    public SaResult LoginEmail( @RequestBody EmailAndPwd emailAndPwd, String identity){
        return userService.LoginMail(emailAndPwd.getEmail(), emailAndPwd.getPwd(), identity);
    }

    @RequestMapping(value ="editMyInfo", method= RequestMethod.POST)
    public SaResult editMyInfo(String bio, String phoneNumber, String sex, String name){
        return ReturnHelper.returnBool(userService.editMyInfo(new UserInfo(bio, name, phoneNumber, sex)));
    }

    @RequestMapping(value ="getMyInfo", method= RequestMethod.GET)
    public SaResult getMyInfo(){
        return ReturnHelper.returnObj(userService.getMyInfo());
    }

    @RequestMapping(value ="getUserByGroup", method= RequestMethod.GET)
    public SaResult getUserByGroup(String groupName){
        return userService.getUserByGroup(groupName);
    }

    // find back pwd
}
