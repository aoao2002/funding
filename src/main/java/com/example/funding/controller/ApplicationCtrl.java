package com.example.funding.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.example.funding.Util.Handler.ReturnHelper;
import com.example.funding.service.Application.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/application/")
public class ApplicationCtrl {
    @Autowired
    private ApplicationService applicationService;

    @RequestMapping(value = "edit/getAppInfoByNumber", method = RequestMethod.GET)
    @ResponseBody
    public SaResult getAppInfoByNumber(String expendNumber){
//        TODO test
        return ReturnHelper.returnObj(applicationService.getAppInfoByNumber(expendNumber, StpUtil.getLoginIdAsLong()));
    }

    @RequestMapping(value ="edit/submitApplication", method= RequestMethod.POST)
    @ResponseBody
    public SaResult submitApplication(String expenditureNumber, String comment,double applyAmount){
        /* TODO 填写支出类别，内容摘要、支出金额、备注；
        自动填 组，经办人；申请提交之后就更新数据库信息
        TDOO 还需要添加方法，选择基金后返回基金的课题组、使用额度等相关信息，
         */
        return ReturnHelper.returnBool(false);
    }
    @RequestMapping(value ="edit/withdrawApplication", method= RequestMethod.POST)
    @ResponseBody
    public SaResult withdrawApplication(){
        // TODO
        return ReturnHelper.returnBool(false);
    }

    @RequestMapping(value ="edit/newExpenditureApplication", method= RequestMethod.POST)
    @ResponseBody
    public SaResult newExpenditureApplication(String expenditureName, String GroupName, String expenditureNumber,
                                              double expenditureTotalAmount, String BeginTime, String EndTime){
        // TODO
        return ReturnHelper.returnBool(false);
    }
}
