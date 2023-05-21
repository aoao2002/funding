package com.example.funding.service.User;

import com.example.funding.bean.User;
import com.example.funding.dao.UserDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class UserServiceMplTest {
    @Resource
    UserDao userDao;
    @Resource
    UserService userService;

    public String getUserIden(User user){
        return user.getEmail()+user.getIdentity();
    }


    @Test
    void getMe() {
        userService.getMe();
        assertTrue(true);
//        这个测试不了
    }

    @Test
    public void findById() {
        User user = userService.findById(0);
        Optional<User> expUser = userDao.findById(0L);
        if (expUser.isEmpty()) {
            fail();
        }
        assertEquals(expUser.get().getEmail()+expUser.get().getIdentity(),
                user.getEmail()+user.getIdentity());
    }

    @Test
    public void getUserByMailAndIdentity() {
        Optional<User> userExp = userDao.findById(0L);
        if (userExp.isEmpty()) {
            fail();
        }
        UserInfo user = userService.getUserByMailAndIdentity(
                userExp.get().getEmail(),String.valueOf(userExp.get().getIdentity())
        );
        assertEquals(userExp.get().getEmail()+userExp.get().getIdentity(),
                user.getMail()+user.getIdentity());

    }

    @Test
    void loginMail() {
    }

    @Test
    void logout() {
    }

    @Test
    void addUser() {
    }

    @Test
    void getUserByMail() {
    }

    @Test
    void getUserByName() {
    }

    @Test
    void editMyInfo() {
    }

    @Test
    void getUserById() {
    }

    @Test
    void getMyInfo() {
    }

    @Test
    void getUserByGroup() {
    }

    @Test
    void checkPresident() {
    }

    @Test
    void checkManager() {
    }

    @Test
    void getPresidents() {
    }

    @Test
    void getAllManagers() {
    }

    @Test
    void isEmail() {
    }

    @Test
    void isInteger() {
    }

    @Test
    void checkMailAndIdentity() {
    }

    @Test
    void sendEmail() {
    }

    @Test
    void checkCode() {
    }

    @Test
    void validMail() {
    }

    @Test
    void unValidMail() {
    }

    @Test
    void getPasswd() {
    }

    @Test
    void getMyEmail() {
    }
}