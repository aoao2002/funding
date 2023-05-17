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

import javax.mail.MessagingException;
import java.io.IOException;

@RestController
@RequestMapping("user/")
public class UserCtrl {
    private static final Logger logger = LoggerFactory.getLogger(UserCtrl.class);

    @Autowired
    private UserService userService;

//    TODO 之后注册时，首先将account设置为abnormal
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

    @RequestMapping(value ="getUserByMailAndIdentity", method= RequestMethod.GET)
    public SaResult getUserByMailAndIdentity(String mail, String identity){
        return ReturnHelper.returnObj(userService.getUserByMailAndIdentity(mail, identity));
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

    @RequestMapping(value ="edit/getPresidents", method= RequestMethod.GET)
    @ResponseBody
    public SaResult getPresidents(){
        return userService.getPresidents();
    }

    @RequestMapping(value ="edit/getAllManagers", method= RequestMethod.GET)
    @ResponseBody
    public SaResult getAllManagers(){
        return userService.getAllManagers();
    }

//    send email
    /*
    TODO 这里验证码就放在User里，对应一个真实邮箱。
    TODO 避免大量的邮箱申请，同一用户限制需求邮箱的时间
    1. 找回密码通过这个邮箱
    2. 创建账户验证邮箱，可以先创建User
     */
    @RequestMapping(value ="sendEmail", method= RequestMethod.POST)
    @ResponseBody
    public SaResult sendEmail(String mail, String identity) throws MessagingException, IOException {
        return userService.sendEmail(mail, identity);
    }

    @RequestMapping(value ="checkCode", method= RequestMethod.POST)
    @ResponseBody
    public SaResult checkCode(String mail, String identity, String code) {
        return userService.checkCode(mail, identity, code);
    }

    @RequestMapping(value ="validMail", method= RequestMethod.POST)
    @ResponseBody
    public SaResult validMail(String mail, String identity) {
        return userService.validMail(mail, identity);
    }
    @RequestMapping(value ="unValidMail", method= RequestMethod.POST)
    @ResponseBody
    public SaResult unValidMail(String mail, String identity) {
        return userService.unValidMail(mail, identity);
    }
    @RequestMapping(value ="getPasswd", method= RequestMethod.POST)
    @ResponseBody
    public SaResult getPasswd(String mail, String identity) {
        return userService.getPasswd(mail, identity);
    }

}
