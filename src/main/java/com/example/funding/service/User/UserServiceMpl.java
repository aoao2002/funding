package com.example.funding.service.User;

import cn.dev33.satoken.util.SaResult;
import org.springframework.stereotype.Service;

@Service
public class UserServiceMpl implements UserService{

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
}
