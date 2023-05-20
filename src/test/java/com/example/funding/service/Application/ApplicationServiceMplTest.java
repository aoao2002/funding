package com.example.funding.service.Application;

import cn.dev33.satoken.util.SaResult;
import com.example.funding.bean.Expenditure;
import com.example.funding.bean.User;
import com.example.funding.dao.ExpenditureDao;
import com.example.funding.dao.UserDao;
import io.swagger.models.auth.In;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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

    List<Long> appId = new ArrayList<>();
    long userId;

    @Test
    @Before
    void testSubmitApplication() throws Exception {
        /*
        SaResult submitApplication(String expendNumber, String expendCategory1, String expendCategory2, String abstrac ,
                                     String comment, String amount, long userId);

        其次 多申请几个并且得到这些app的id；
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