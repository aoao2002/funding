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

import java.text.ParseException;

/**
 * TODO 通过test类来检查，手动测试太麻烦了
 * 有的方法：app/expend 的新建，申请，
 * 1. getAppInfoByNumber(String expendNumber)：填表时通过expendNumber获得该expend相关的其他信息
 * 2. submitApplication：填写申请的其余部分
 * 3. withdrawApplication：撤销，通过app_id
 * 4. newExpenditureApplication：新建expend
 */


@RestController
@RequestMapping("/application/")
public class ApplicationCtrl {
    @Autowired
    private ApplicationService applicationService;

    @RequestMapping(value = "edit/getAppInfoByNumber", method = RequestMethod.GET)
    @ResponseBody
    public SaResult getAppInfoByNumber(String expendNumber){
//        TODO test
        /*
        填写申请时，先填写exp_number，之后会返回相关信息，然后再继续填写相应的信息即可
         */
        return ReturnHelper.returnObj(applicationService.getAppInfoByNumber(expendNumber, StpUtil.getLoginIdAsLong()));
    }

    /* TODO 填写支出类别，内容摘要、支出金额、备注；实现中save过程怎么判断是否成功，测试-注意观察id
        NOTE 返回app的id，这里提交之后就需要在expend里面实时修改
        自动填组，经办人；申请提交之后就更新数据库信息
        TODO 测试：直接exp操作能实现金额修改吗
    */
    @RequestMapping(value ="edit/submitApplication", method= RequestMethod.POST)
    @ResponseBody
    public SaResult submitApplication(String expenditureNumber, int cate, String abstrac, String comment,double applyAmount){
        return ReturnHelper.returnObj(applicationService.submitApplication(expenditureNumber, cate,
                abstrac, comment, applyAmount, StpUtil.getLoginIdAsLong()));
    }
    @RequestMapping(value ="edit/withdrawApplication", method= RequestMethod.POST)
    @ResponseBody
    public SaResult withdrawApplication(long appId){
        // TODO（或者管理者处分析app状态，然后管理者按键操作）
        return ReturnHelper.returnBool(applicationService.withdrawApplication(appId));
    }

    /*
       建立新的expend，填写具体的expend 名称
       报错：
       1. 填写的组别不属于该用户属于的组
       2. 填写的时间不对
       3. 该经费不存在（这个交给上面去审核）
       TODO 注意管理者需要给expend设置每年的限额，这个先交到manager的set里，然后再交到对应的Group里【manager确认之后】
       NOTE 这里所有的申请审批都是 加到对应人里，有人修改之后，申请状态改变，对应人每次处理时会先筛选申请表的状态
    */
    @RequestMapping(value ="edit/newExpenditureApplication", method= RequestMethod.POST)
    @ResponseBody
    public SaResult newExpenditureApplication(String expenditureName, String groupName, String expenditureNumber,
                                              double expenditureTotalAmount, String beginTime, String endTime) throws ParseException {
        return ReturnHelper.returnObj(applicationService.newExpenditureApplication(expenditureName, groupName, expenditureNumber,
                expenditureTotalAmount, beginTime, endTime, StpUtil.getLoginIdAsLong()));
    }
}
