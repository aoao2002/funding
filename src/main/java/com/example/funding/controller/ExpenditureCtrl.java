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
    // 修改部分经费信息，管理员以上才能改quota，endTime是否开放给staff再议
    @RequestMapping(value ="edit/updateExpenditureQuota", method= RequestMethod.POST)
    @ResponseBody
    public SaResult updateExpenditureQuota(String expenditureNumber, String quota){
        return ReturnHelper.returnBool(expenditureService.updateExpenditureQuota(expenditureNumber,quota));
    }
    @RequestMapping(value ="edit/updateExpenditureEndTime", method= RequestMethod.POST)
    @ResponseBody
    public SaResult updateExpenditureEndTime(String expenditureNumber, String endTime){
        return ReturnHelper.returnBool(expenditureService.updateExpenditureEndTime(expenditureNumber,endTime));
    }


    // 展示经费使用情况 还有很多种展示方式
    @RequestMapping(value ="view/getOneExpenditureAllInfo", method= RequestMethod.POST)
    @ResponseBody
    public SaResult getOneExpenditureAllInfo(String expenditureNumber){
        return ReturnHelper.returnObj(expenditureService.getOneExpenditureAllInfo(expenditureNumber));
    }
    @RequestMapping(value ="view/getAllExpenditureInfoInOneGroup", method= RequestMethod.POST)
    @ResponseBody
    public SaResult getAllExpenditureInfoInOneGroup(String groupName){
        return ReturnHelper.returnObj(expenditureService.getAllExpenditureInfoInOneGroup(groupName));
    }

    //超级管理员才能用这个
    @RequestMapping(value ="view/getAllExpenditureInfo", method= RequestMethod.POST)
    @ResponseBody
    public SaResult getAllExpenditureInfo(){
        return ReturnHelper.returnObj(expenditureService.getAllExpenditureInfo());
    }
    //增长率
    //饼状图

}
