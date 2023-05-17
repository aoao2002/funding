package com.example.funding.service.User;

import cn.dev33.satoken.util.SaResult;
import com.example.funding.bean.User;
import com.example.funding.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public interface UserService {

    SaResult LoginMail(String Mail, String pwd, String identity);

    SaResult Logout();

    SaResult addUser(String email, String pwd, String name, String identity);

    List<UserInfo> getUserByMail(String email);

    List<UserInfo> getUserByName(String name);

    boolean editMyInfo(UserInfo userInfo);

    UserInfo getUserById(long id);

    UserInfo getMyInfo();

    SaResult getUserByGroup(String groupName);

    boolean checkPresident();

    boolean checkManager();

    User findById(long userID);

    UserInfo getUserByMailAndIdentity(String email, String identity);

    User getMe();

    SaResult getPresidents();

    SaResult getAllManagers();

    /*
      Email related
      1. 注册时验证邮箱是否可用，首先后端创建该用户，但是valid位置为 0，然后进行邮箱验证【拆成：创建用户，邮箱验证，valid改为1-》账户可用】
      2. 找回密码时，首先通过邮箱验证，之后可以重置密码【split：邮箱验证，重置密码】

      - getCode (获得验证码)  {String mail, int identity}
        if date > user.codeTime + 5s:
            code = random()
            sendEmail
         else
            return error
     */
    SaResult sendEmail(String mail, String identity) throws IOException, MessagingException;
    SaResult checkCode(String mail, String identity, String code);
    SaResult validMail(String mail, String identity);
    SaResult unValidMail(String mail, String identity);
    SaResult getPasswd(String mail, String identity);

}
