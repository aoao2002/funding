package com.example.funding.controller;


import cn.dev33.satoken.stp.StpUtil;
import com.example.funding.service.User.EmailAndPwd;
import com.example.funding.service.User.RegisterInfo;
import com.example.funding.service.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import cn.dev33.satoken.util.SaResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@RestController
@RequestMapping("/user/")
public class UserCtrl {

    private static final Logger logger = LoggerFactory.getLogger(UserCtrl.class);

    @Autowired
    private UserService userService;

    @RequestMapping(value ="register", method= RequestMethod.POST)
    public SaResult register(@RequestBody RegisterInfo registerInfo){
        return userService.addUser(registerInfo.getEmail(),
                registerInfo.getPwd(), registerInfo.getName());
    }

    @RequestMapping(value ="logout", method= RequestMethod.POST)
    public SaResult Logout(){
        StpUtil.logout();
        return SaResult.ok("logout success");
    }

    @RequestMapping(value ="getUserInfo", method= RequestMethod.GET)
    public SaResult getUserInfo(String mail, String name){
        // TODO
        // 1. check if mail exists
        // 2. check if name exists
        // 3. if both exists, return some user info
//        UserInfo result = null;
//        if(InputChecker.checkNullAndEmpty(mail))
//            result = userService.getUserByMail(mail);
//        else if(InputChecker.checkNullAndEmpty(name))
//            result = userService.getUserByName(name);
//
//        if (result == null) {
//            return SaResult.error("no such user");
//        }
        return SaResult.ok().setData(null);
    }
}
