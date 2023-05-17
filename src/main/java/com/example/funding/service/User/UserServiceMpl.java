package com.example.funding.service.User;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.example.funding.Util.Exception.BeanException;
import com.example.funding.Util.Handler.InputChecker;
import com.example.funding.bean.Group;
import com.example.funding.bean.User;
import com.example.funding.dao.GroupDao;
import com.example.funding.dao.UserDao;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceMpl implements UserService{
    private static final Logger logger = LoggerFactory.getLogger(UserServiceMpl.class);

    @Autowired
    private UserDao userDao;

    @Autowired
    private GroupDao groupDao;

    public User getMe(){
        return findById(StpUtil.getLoginIdAsLong());
    }

    public User findById(long userID){
        Optional<User> us = userDao.findById(userID);
        if(us.isEmpty()){
            throw new BeanException("the User do not exist");
        }
        return us.get();
    }

    @Override
    public UserInfo getUserByMailAndIdentity(String email, String identity) {
        if(!InputChecker.checkNullAndEmpty(Lists.newArrayList(email, identity)))
            return null;

        if(!InputChecker.checkNum(identity))
            return null;

        int identity_int = Integer.parseInt(identity);
        User user = userDao.findByEmailAndIdentity(email, identity_int);

        return new UserInfo(user);
    }

    public SaResult LoginMail(String Mail, String pwd, String identity){
        if(!InputChecker.checkNullAndEmpty(Lists.newArrayList(Mail, pwd, identity)))
            return SaResult.error("login fail: input Null or Empty");;
        User user = userDao.findByEmailAndIdentity(Mail, Integer.parseInt(identity));
        if (user==null) return SaResult.error("login fail: no such user");
        //check pw
        UserInfo userInfo = new UserInfo(user);
        if (user.getEmail().equals(Mail) && user.getPw().equals(pwd)){
            // login
            StpUtil.login(user.getId());
            // get token
            SaTokenInfo tokenInfo = StpUtil.getTokenInfo();

            return SaResult.ok().setData(new LoginInfo(tokenInfo,userInfo.getName()));
        }else {
            return SaResult.error("login fail: pwd error");
        }
    }

    @Override
    public SaResult Logout() {
        StpUtil.logout();
        return SaResult.ok("注销成功");
    }

    public SaResult addUser(String email, String pwd, String name, String identity){

        if(!InputChecker.checkNullAndEmpty(Lists.newArrayList(email, pwd, name, identity)))
            return SaResult.error("register error: input Null or Empty");

        // check mail
        if (!email.matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$")){
            return SaResult.error("register error: mail format error");
        }

        // check if user exist
        User user = userDao.findByEmailAndIdentity(email, Integer.parseInt(identity));

        // could not register as president
        if(Integer.parseInt(identity) == User.getPresidentIdentity())
            return SaResult.error("could not register as president");

        if (user==null) {
            User new_user = new User();
            new_user.setName(name);
            new_user.setPw(pwd);
            new_user.setEmail(email);
            new_user.setIdentity(Integer.parseInt(identity));
            userDao.save(new_user);
            return SaResult.ok("register success");
        }

        return SaResult.error("register error: user already exist");
    }

    @Override
    public List<UserInfo> getUserByMail(String email) {
        if(!InputChecker.checkNullAndEmpty(Lists.newArrayList(email)))
            return null;

        List<User> users = userDao.findAllByEmail(email);

        return users!=null ? users.stream().map(UserInfo::new).collect(Collectors.toList()) : null;
    }

    @Override
    public List<UserInfo> getUserByName(String name) {
        if(!InputChecker.checkNullAndEmpty(Lists.newArrayList(name))){
            return null;
        }

        List<User> users = userDao.findAllByName(name);

        ArrayList<UserInfo> userInfos = new ArrayList<>();
        users.forEach(
                user -> {
                    userInfos.add(new UserInfo(user));
                }
        );
        return userInfos;
    }

    @Override
    public boolean editMyInfo(UserInfo userInfo) {

        if (userInfo==null) return false;
        User me = getMe();
        if(me==null) return false;
        userDao.save(userInfo.changeUser(me));
        return true;
    }

    @Override
    public UserInfo getUserById(long id) {
        Optional<User> user = userDao.findById(id);
        if(user.isEmpty()){
            System.out.println("no user with this id");
            return null;
        }
        return new UserInfo(user.get());
    }

    @Override
    public UserInfo getMyInfo() {
        return getUserById(getMe().getId());
    }

    @Override
    public SaResult getUserByGroup(String groupName) {
        if(!InputChecker.checkNullAndEmpty(Lists.newArrayList(groupName)))
            return SaResult.error("getUserByGroup fail: input Null or Empty");

        Group group = groupDao.findByName(groupName);
        if(group==null) return SaResult.error("getUserByGroup fail: no such group");

        List<User> users = userDao.findAllByGroups(group);
        List<UserInfo> userInfos = new ArrayList<>();
        logger.error(users.size()+"");

        users.forEach(
                user -> {
                    logger.info(user.getName());
                    userInfos.add(new UserInfo(user));
                }
        );
        return SaResult.data(userInfos);
    }

    @Override
    public boolean checkPresident() {
        return getMyIdentity() == 2;
    }

    private int getMyIdentity() {
        User me = getMe();
        return me.getIdentity();
    }

    public SaResult getPresidents(){
        List<User> userSet = userDao.findByIdentity(2);
        Set<UserInfo> userInfos = new HashSet<>();
        userSet.forEach(s->userInfos.add(new UserInfo(s)));
        return SaResult.ok().setData(userInfos);
    }

    public SaResult getAllManagers(){
        List<User> userSet = userDao.findByIdentity(1);
        Set<UserInfo> userInfos = new HashSet<>();
        userSet.forEach(s->userInfos.add(new UserInfo(s)));
        return SaResult.ok().setData(userInfos);
    }


}
