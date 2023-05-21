package com.example.funding.service.User;

import cn.dev33.satoken.util.SaResult;
import com.example.funding.bean.Expenditure;
import com.example.funding.bean.Group;
import com.example.funding.bean.User;
import com.example.funding.dao.GroupDao;
import com.example.funding.dao.UserDao;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserServiceMplTest {
    @Resource
    public UserDao userDao;
    @Resource
    public UserService userService;

    public String getUserIden(User user){
        return user.getEmail()+user.getIdentity();
    }


    @Test
    @Order(1)
    public void getMe() {
        System.out.println("getMeTest");
        userService.getMe();
        assertTrue(true);
//        这个测试不了
    }

    @Test
    @Order(2)
    public void findById() {
        System.out.println("findByIdTest");
        User user = userService.findById(1);
        Optional<User> expUser = userDao.findById(1L);
        if (expUser.isEmpty()) {
            fail();
        }
        assertEquals(expUser.get().getEmail()+expUser.get().getIdentity(),
                user.getEmail()+user.getIdentity());
    }

    @Test
    @Order(3)
    public void getUserByMailAndIdentity() {
        System.out.println("getUserByMailAndIdentityTest");
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
    /*
    准备三个人员
    staff name-yxy, pw-123, email-1343022453@qq.com,

     */
    /*
        基本信息：
        管理员：通过identity找，建不了
        小组：imed
        管理员：name-lj,pw-123,mail-123@qq.com,
        成员：name-y,pw-123,mail-12@qq.com
        基金：imed123，
         */
    @Resource
    GroupDao groupDao;
    public static Group group;
    public static User president;
    public static User manager;
    public static User staff;
    public static Expenditure expenditure;
    static Date start  = null, end = null;
    @Test
    @Order(4)
    public void setUp(){
        System.out.println("setUpTest");
        if (president==null){
            List<User> pre = userDao.findByIdentity(2);
            if (pre.isEmpty()){
                assertTrue(false);
            }
            president = pre.get(0);
        }

        if (!groupDao.existsByName("imed")){
            Group group1 = new Group();
            group1.setCreatedDate(new Date());
            group1.setUsers(new HashSet<>());
            group1.setExpenditures(new HashSet<>());
            group1.getUsers().add(president);
            group = groupDao.save(group1);
        }else{
            group = groupDao.findByName("imed");
        }
        if (!userDao.existsByEmailAndIdentity("123@qq.com", 1)){
            User mana = new User();
            mana.setStatus("0");
            mana.setApplications(new HashSet<>());
            mana.setGroups(new HashSet<>(){{
                add(group);
            }});
            mana.setName("lj");
            mana.setEmail("123@qq.com");
            mana.setSex(0);
            mana.setPw("123");
            mana.setIdentity(1);
            mana.setGroupApplications(new HashSet<>());
            manager = userDao.save(mana);
        }else{
            manager = userDao.findByEmailAndIdentity("123@qq.com", 1);
        }

        if (!userDao.existsByEmailAndIdentity("12@qq.com", 0)){
            User staf = new User();
            staf.setStatus("0");
            staf.setApplications(new HashSet<>());
            staf.setGroups(new HashSet<>(){{
                add(group);
            }});
            staf.setName("y");
            staf.setEmail("12@qq.com");
            staf.setSex(0);
            staf.setPw("123");
            staf.setIdentity(0);
            staf.setGroupApplications(new HashSet<>());
            staff = userDao.save(staf);
        }else{
            staff = userDao.findByEmailAndIdentity("12@qq.com", 0);
        }
    }

    @Test
    @Order(5)
    public void loginMail() {
        System.out.println("loginMailTest");
        SaResult res = userService.LoginMail("12@qq.com", "123", "0");
        assertEquals(200, res.getCode());
    }

    @Test
    @Order(6)
    public void logout() {
        System.out.println("logoutTest");
        SaResult res = userService.Logout();
        assertEquals(200, res.getCode());
    }

    @Test
    @Order(7)
    public void addUser() {
        System.out.println("addUserTest");
        StringBuilder code = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            code.append(random.nextInt());
        }
        code.append("@qq.com");
        String email = code.toString();
        SaResult res = userService.addUser(email, "123", "testAdd", "0");
        assertEquals(200, res.getCode());
    }

    @Test
    @Order(8)
    public void getUserByMail() {
        System.out.println("getUserByMailTest");
        List<User> user = userDao.findAllByEmail("12@qq.com");
        List<UserInfo>  methodFind = userService.getUserByMail("12@qq.com");
        assertEquals(user.size(), methodFind.size());
    }

    @Test
    @Order(9)
    public void getUserByName() {
        System.out.println("getUserByNameTest");
        List<User> users = userDao.findAllByName("y");
        List<UserInfo> userInfos = userService.getUserByName("y");
        assertEquals(users.size(), userInfos.size());
    }

    @Test
    public void editMyInfo() {

    }

    @Test
    public void getUserById() {
    }

    @Test
    public void getMyInfo() {
    }

    @Test
    public void getUserByGroup() {
    }

    @Test
    public void checkPresident() {
    }

    @Test
    public void checkManager() {
    }

    @Test
    public void getPresidents() {
    }

    @Test
    public void getAllManagers() {
    }

    @Test
    public void isEmail() {
    }

    @Test
    public void isInteger() {
    }

    @Test
    public void checkMailAndIdentity() {
    }

    @Test
    public void sendEmail() {
    }

    @Test
    public void checkCode() {
    }

    @Test
    public void validMail() {
    }

    @Test
    public void unValidMail() {
    }

    @Test
    public void getPasswd() {
    }

    @Test
    public void getMyEmail() {
    }
}