package com.example.funding.controller;

import org.junit.jupiter.api.Test;

public class TestExpenditure {
    /**
     * long newExpenditureApplication(String expenditureName, String GroupName, String expenditureNumber,
     *                               double expenditureTotalAmount, String BeginTime, String EndTime, long userId) throws ParseException;
     * 1. expName/expNum 非法，但是暂时检查不了
     * 2. groupName 不属于这个人的组
     * 3. 时间不对
     *
     * Test:插入随机信息
     */
    @Test
    public void testExpend(){

    }
}
