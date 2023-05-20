package com.example.funding.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/*
准备基础人员，组别，基金以及申请

 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestApplication {

    /**
     * AppInfo getAppInfoByNumber(String expendNumber, long staffId)
     * 1. ependNumber 不存在
     * 2. 这个人的身份查看不了这个expend
     */
    @Test
    public void testGetAppInfoByNum(){

    }

    /**
     * long submitApplication(String expendNumber, int expendCategory, String abstrac ,
     *                                      String comment, double amount, long userId)
     * 1. expNum 不存在
     * 2. expCat 非法
     * 3. amount超过限额
     * 4. 用户id不能使用该基金（但是不可能，因为在上面那个方法已经判断过了）
     */
    @Test
    public void testSubApp(){

    }

    /**
     * boolean withdrawApplication(long appId);
     * 1. appId 不存在
     * 2. appId不属于这个人
     */
    @Test
    public void testWithdrawApp(){

    }


}
