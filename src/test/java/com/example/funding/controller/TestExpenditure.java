package com.example.funding.controller;

import com.example.funding.service.Application.ApplicationService;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang3.RandomStringUtils;

import java.text.ParseException;

public class TestExpenditure {
    /**
     * long newExpenditureApplication(String expenditureName, String GroupName, String expenditureNumber,
     *                               double expenditureTotalAmount, String BeginTime, String EndTime, long userId) throws ParseException;
     * 1. expName/expNum 非法，但是暂时检查不了
     * 2. groupName 不属于这个人的组
     * 3. 时间不对
     *
     * SaResult newExpenditureApplication(String expenditureName, String groupName, String expenditureNumber,
     *                                               double expenditureTotalAmount, String beginTime, String endTime)
     *
     * Test:插入随机信息，对于组别都有
     */
    @Autowired
    ApplicationCtrl applicationCtrl;
    @Autowired
    ApplicationService applicationService;


//    参演：group-imed/1, mana-aoao/4, staff-y/1
    String groupName = "imed";
    String managerName = "aoao";
    String staffName = "y";

    @Test
    public void testExpend() throws ParseException {
        double amount = 10000;
        String beginTime = "2022-02-22 12:00:00", endTime = "2023-02-02 12:11:12";
//        先插入一个合法的，
        String expName = "national_nature", expNumber = RandomStringUtils.random(10);
        long res = applicationService.newExpenditureApplication(expName, groupName, expNumber,
                amount, beginTime, endTime, 1L);
        Assertions.assertTrue(res > -1);




    }
}
