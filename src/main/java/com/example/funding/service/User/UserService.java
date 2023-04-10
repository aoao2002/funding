package com.example.funding.service.User;

import cn.dev33.satoken.util.SaResult;
import com.example.funding.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {

    SaResult LoginMail(String Mail, String pwd, String identity);
    SaResult Logout();

    SaResult addUser(String email, String pwd, String name, String identity);

    UserInfo getUserByMail(String email);
    List<UserInfo> getUserByName(String name);

    boolean editMyInfo(UserInfo userInfo);

    UserInfo getUserById(long id);

    UserInfo getMyInfo();



}
