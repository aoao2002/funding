package com.example.funding.controller;

import cn.dev33.satoken.util.SaResult;
import com.example.funding.Util.Handler.ReturnHelper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/expenditure/")
public class ExpenditureCtrl {
    // 展示经费使用情况 还有很多种展示方式
    @RequestMapping(value ="view/showExpenditure", method= RequestMethod.POST)
    @ResponseBody
    public SaResult showExpenditure(Set<String> groups_name, Set<String> expenditure_name){
        // TODO
        return ReturnHelper.returnBool(false);
    }

    //getBasicExpenditureInfo --> bean
    //增长率
    //饼状图

}
