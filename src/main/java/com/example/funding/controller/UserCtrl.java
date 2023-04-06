package com.example.funding.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/")
public class UserCtrl {
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
    public boolean loginEmail(String email, String password){
        // TODO
        // 1. check if email exists
        // 2. check if password is correct
        // 3. if correct, set session
        return false;
    }
    @RequestMapping(value ="logout", method= RequestMethod.POST)
    @ResponseBody
    public boolean logout(){
        // TODO
        // 1. clear session
        return false;
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
