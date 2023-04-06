package com.example.funding.service.User;

import cn.dev33.satoken.util.SaResult;
import com.example.funding.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    SaResult LoginMail(String Mail, String pwd);
    SaResult Logout();

    SaResult addUser(String email, String pwd, String name);

}
