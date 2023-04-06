package com.example.funding.service.User;

import cn.dev33.satoken.util.SaResult;
import com.example.funding.Util.Handler.InputChecker;
import com.example.funding.bean.User;
import com.example.funding.dao.UserDao;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserServiceMpl implements UserService{
    @Autowired
    private UserDao userDao;

    public SaResult LoginMail(String Mail, String pwd){
        // TODO
        // 1. check if email exists
        // 2. check if password is correct
        // 3. if correct, set session

        return SaResult.error("login fail: pwd error");
    }

    @Override
    public SaResult Logout() {

        // TODO
        // 1. clear session
        return null;
    }

    public SaResult addUser(String email, String pwd, String name){

        if(!InputChecker.checkNullAndEmpty(Lists.newArrayList(email, pwd, name)))
            return SaResult.error("register error: input Null or Empty");

        // check mail
        if (!email.matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$")){
            return SaResult.error("register error: mail format error");
        }

        // check if user mail exist
        User user = userDao.findByEmail(email);

        if (user==null) {
            User new_user = new User();
            new_user.setName(name);
            new_user.setPw(pwd);
            new_user.setEmail(email);
            userDao.save(new_user);
            return SaResult.ok("register success");
        }

        return SaResult.error("register error: user already exist");
    }
}
