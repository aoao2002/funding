package com.example.funding.service.Application;

import cn.dev33.satoken.util.SaResult;
import com.example.funding.bean.Expenditure;
import com.example.funding.bean.Group;
import com.example.funding.bean.User;
import com.example.funding.dao.ExpenditureDao;
import com.example.funding.dao.GroupDao;
import com.example.funding.dao.UserDao;
import io.swagger.models.auth.In;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@RunWith(SpringRunner.class)
class ApplicationServiceMplTest {
    @Resource
    ApplicationService applicationService;

    @Resource
    ExpenditureDao expenditureDao;
    @Resource
    UserDao userDao;
    @Resource
    GroupDao groupDao;

    List<Long> appId = new ArrayList<>();
    long userId;

     /*
        基本信息：
        管理员：通过identity找，建不了
        小组：imed
        管理员：name-lj,pw-123,mail-123@qq.com,
        成员：name-y,pw-123,mail-12@qq.com
        基金：imed123，
         */
    private Group group;
    private User president;
    private User manager;
    private User staff;
    private Expenditure expenditure;


    @Test
    void basicSetUp(){
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
        if (!expenditureDao.existsByNumber("imed123")){
            Expenditure expenditure1 = new Expenditure();
            expenditure1.setApplications(new HashSet<>());
            expenditure1.setGroup(group);
            expenditure1.setType(1);
            expenditure1.setStatus(1);
            expenditure1.setName("imed123");
            expenditure1.setTotalAmount(1000000);
            expenditure1.setRemainingAmount(0);
            expenditure1.setQuota(1000000);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date start  = null, end = null;
            try {
                start  = sdf.parse("2017-11-06");
                end = sdf.parse("2030-11-06");
            }catch (Exception e){
                e.printStackTrace();
            }
            expenditure1.setCreatedDate(start);
            expenditure1.setEndTime(end);

        }
    }

    @Test
    @Before
    void testSubmitApplication() throws Exception {
        /*
        SaResult submitApplication(String expendNumber, String expendCategory1, String expendCategory2, String abstrac ,
                                     String comment, String amount, long userId);

        其次 多申请几个并且得到这些app的id；

        这个默认的 基金AB123,
         */
        String[] condtion = {"test_subApp", "100", "hell", "AB123", "Office", "pen"};
        Expenditure expenditure = expenditureDao.findByNumberAndStatus(condtion[3], 1);
        if (expenditure==null){
            throw new Exception("no such expenditure");
        }
        List<User> userList = userDao.findByGroups_NameAndIdentity(expenditure.getGroup().getName(),
                0);
        User caseUser = userList.get(0);
        userId = caseUser.getId();
        SaResult res = applicationService.submitApplication(
                expenditure.getNumber(), condtion[4], condtion[5],
                condtion[0], condtion[2], condtion[1],caseUser.getId()
        );
        appId.add((Long) res.getData());
        SaResult res2 = applicationService.submitApplication(
                expenditure.getNumber(), condtion[4], condtion[5],
                condtion[0], condtion[2], condtion[1],caseUser.getId()
        );
        appId.add((Long) res2.getData());
        SaResult res3 = applicationService.submitApplication(
                expenditure.getNumber(), condtion[4], condtion[5],
                condtion[0], condtion[2], condtion[1],caseUser.getId()
        );
        appId.add((Long) res3.getData());
        assertEquals(200, res.getCode());

    }


    @Test
    void withdrawApplication() {
        SaResult res = applicationService.withdrawApplication(String.valueOf(appId.get(0)));
        assertEquals(200, res.getCode());
    }

    @Test
    void passApplication() {
        SaResult res = applicationService.passApplication(userId, String.valueOf(appId.get(1)), "good job");
        assertEquals(200, res.getCode());
    }

    @Test
    void rejectApplication() {
        SaResult res = applicationService.rejectApplication(userId, String.valueOf(appId.get(2)), "bad jobs");
        assertEquals(200, res.getCode());
    }

    @Test
    void getMyExpendsToExam() {

    }

    @Test
    void submitExpend() {
    }

    @Test
    void passExpenditure() {
    }

    @Test
    void rejectExpenditure() {
    }

    @Test
    void newExpenditureApplication() {
    }

    @Test
    void getAllMyExpends() {
    }
}