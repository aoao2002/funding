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
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@RunWith(SpringRunner.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ApplicationServiceMplTest {
//    NOTE 测试fail可能是因为surefile的依赖，需要将surefile都移除
//    https://stackoverflow.com/questions/45092964/org-apache-maven-lifecycle-lifecycleexecutionexception-failed-to-execute-goal-o

    @Resource
    ApplicationService applicationService;

    @Resource
    ExpenditureDao expenditureDao;
    @Resource
    UserDao userDao;
    @Resource
    GroupDao groupDao;

    static List<Long> appId = new ArrayList<>();
    static long userId;

     /*
        基本信息：
        管理员：通过identity找，建不了
        小组：imed
        管理员：name-lj,pw-123,mail-123@qq.com,
        成员：name-y,pw-123,mail-12@qq.com
        基金：imed123，
         */
    public static Group group;
    public static User president;
    public static User manager;
    public static User staff;
    public static Expenditure expenditure;
    static Date start  = null, end = null;


    @Test
    @Order(1)
    public void basicSetUp1(){
        if (president==null){
            List<User> pre = userDao.findByIdentity(2);
            if (pre.isEmpty()){
                fail();
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
            expenditure1.setName("imed");
            expenditure1.setNumber("imed123");
            expenditure1.setTotalAmount(1000000);
            expenditure1.setRemainingAmount(100000);
            expenditure1.setQuota(1000000);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            try {
                start  = sdf.parse("2017-11-06");
                end = sdf.parse("2030-11-06");
            }catch (Exception e){
                e.printStackTrace();
            }
            expenditure1.setCreatedDate(start);
            expenditure1.setStartTime(start);
            expenditure1.setEndTime(end);
            expenditure = expenditureDao.save(expenditure1);
        }else{
            expenditure = expenditureDao.findByNumber("imed123").get(0);
        }
    }

    @Test
    @Order(2)
    public void testSubmitApplication2() throws Exception {
        /*
        SaResult submitApplication(String expendNumber, String expendCategory1, String expendCategory2, String abstrac ,
                                     String comment, String amount, long userId);

        其次 多申请几个并且得到这些app的id；

        这个默认的 基金imed123,
         */
        String[] condtion = {"test_subApp", "10", "hell", "imed123", "Office", "pen"};
        if (expenditure==null){
            expenditure = expenditureDao.findByNumber("imed123").get(0);
//            throw new Exception("no such expenditure");
        }
        User caseUser = userDao.findByEmailAndIdentity("12@qq.com", 0);
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
        System.out.println(res.getMsg());
        assertEquals(200, res.getCode());

    }


    @Test
    @Order(3)
    public void withdrawApplication() {
        SaResult res = applicationService.withdrawApplication(String.valueOf(appId.get(0)));
        assertEquals(200, res.getCode());
    }

    @Test
    @Order(4)
    public void passApplication() {
        userId = manager.getId();
        SaResult res = applicationService.passApplication(userId, String.valueOf(appId.get(1)), "good job");
        System.out.println(res.getMsg());
        assertEquals(200, res.getCode());
    }

    @Test
    @Order(5)
    public void rejectApplication() {
        SaResult res = applicationService.rejectApplication(userId, String.valueOf(appId.get(2)), "bad jobs");
        System.out.println(res.getMsg());
        assertEquals(200, res.getCode());
    }

    static List<String> testExpNumber;
    static List<Long> testExpId;

    @Test
    @Order(6)
    public void submitExpend3() throws ParseException {
        testExpNumber = new ArrayList<>();
        testExpId = new ArrayList<>();
        Random random = new Random();
        StringBuilder code = new StringBuilder();
//        申请会被通过的expend
        for (int i = 0; i < 6; i++) {
            code.append(random.nextInt(10));
        }
        testExpNumber.add(code.toString());
        if (start == null){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            start = sdf.parse("2017-10-10 12:00:00");
            end = sdf.parse("2030-10-10 12:00:00");
        }
        SaResult res1 = applicationService.submitExpend("pass", code.toString(), "10",
                start.toString(), end.toString(), group.getName(), staff.getId());
        testExpId.add(((ExpendInfo)res1.getData()).getExpendId());
        System.out.println(res1.getMsg());
//        NOTE check 是否可以提交
        assertEquals(200, res1.getCode());
//        申请会被拒绝的expend
        code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            code.append(random.nextInt(10));
        }
        testExpNumber.add(code.toString());
        SaResult res2 = applicationService.submitExpend("reject", code.toString(), "10",
                start.toString(), end.toString(), group.getName(), staff.getId());
        testExpId.add(((ExpendInfo)res2.getData()).getExpendId());

    }

    @Test
    @Order(7)
    public void getMyExpendsToExam() {
        SaResult res = applicationService.getAllMyExpends(staff.getId());
        List<ExpendInfo> resExp = (List<ExpendInfo>) res.getData();
        System.out.println(res.getMsg());
        assertTrue(resExp.stream().map(ExpendInfo::getExpendId).toList().containsAll(testExpId));
    }

    @Test
    @Order(8)
    public void passExpenditure() {
        SaResult res = applicationService.passExpenditure(manager.getId(), String.valueOf(testExpId.get(0)));
        System.out.println(res.getMsg());
        assertEquals(200, res.getCode());
    }

    @Test
    @Order(9)
    public void rejectExpenditure() {
        SaResult res = applicationService.rejectExpenditure(manager.getId(), String.valueOf(testExpId.get(1)));
        System.out.println(res.getMsg());
        assertEquals(200, res.getCode());
    }

    @Test
    @Order(10)
    public void getAllMyExpends() {
        SaResult res = applicationService.getAllMyExpends(staff.getId());
        List<Long> resExpId = ((List<ExpendInfo>)res.getData()).stream().map(ExpendInfo::getExpendId).toList();
        System.out.println(res.getMsg());
        assertTrue(resExpId.containsAll(testExpId));
    }

    @Test
    void deleteAllTestExpends(){

    }
}