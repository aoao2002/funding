package com.example.funding.controller;


import com.example.funding.service.User.EmailAndPwd;
import com.example.funding.service.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import cn.dev33.satoken.util.SaResult;

@RestController
@RequestMapping("/user/")
public class UserCtrl {

    @Autowired
    private UserService userService;

    @RequestMapping(value ="register", method= RequestMethod.POST)
    @ResponseBody
    public boolean register(String email, String password){
        // TODO
        // 1. check if email exists
        // 2. if not, insert into database
        return false;
    }
    @RequestMapping(value ="loginEmail", method= RequestMethod.POST)
    @ResponseBody
    public SaResult loginEmail(@RequestBody EmailAndPwd emailAndPwd){
        return userService.LoginMail(emailAndPwd.getEmail(), emailAndPwd.getPwd());
    }

    @RequestMapping(value ="logout", method= RequestMethod.POST)
    @ResponseBody
    public SaResult Logout(){
        return userService.Logout();
    }

    @RequestMapping(value ="getUserInfo", method= RequestMethod.GET)
    @ResponseBody
    public boolean getUserInfo(String mail){
        // TODO
        // 1. check if mail exists
        // 2. check if name exists
        // 3. if both exists, return some user info
        return false;
    }
}
