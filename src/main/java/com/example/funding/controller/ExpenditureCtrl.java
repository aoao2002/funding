package com.example.funding.controller;

import cn.dev33.satoken.util.SaResult;
import com.example.funding.Util.Handler.ReturnHelper;
import com.example.funding.service.Expenditure.ExpenditureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/expenditure/")
public class ExpenditureCtrl {
    @Autowired
    private ExpenditureService expenditureService;
    // 展示经费使用情况 还有很多种展示方式
    @RequestMapping(value ="view/getOneExpenditureAllInfo", method= RequestMethod.POST)
    @ResponseBody
    public SaResult getOneExpenditureAllInfo(String expenditureNumber){
        return ReturnHelper.returnObj(expenditureService.getOneExpenditureAllInfo(expenditureNumber));
    }
    //getBasicExpenditureInfo --> bean
    //增长率
    //饼状图

}
