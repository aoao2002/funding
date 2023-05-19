package com.example.funding.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.example.funding.service.Application.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;

/**
 * TODO 通过test类来检查，手动测试太麻烦了/ps测试类更麻烦
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
        return applicationService.getAppInfoByNumber(expendNumber, StpUtil.getLoginIdAsLong());
    }

    /* TODO 填写支出类别，内容摘要、支出金额、备注；实现中save过程怎么判断是否成功，测试-注意观察id
        NOTE 返回app的id，这里提交之后就需要在expend里面实时修改
        自动填组，经办人；申请提交之后就更新数据库信息
        TODO 测试：直接exp操作能实现金额修改吗
    */
    @RequestMapping(value ="edit/submitApplication", method= RequestMethod.POST)
    @ResponseBody
    public SaResult submitApplication(String expenditureNumber, String cate, String abstrac, String comment,String applyAmount){
        return applicationService.submitApplication(expenditureNumber, cate,
                abstrac, comment, applyAmount, StpUtil.getLoginIdAsLong());
    }
    @RequestMapping(value ="edit/withdrawApplication", method= RequestMethod.POST)
    @ResponseBody
    public SaResult withdrawApplication(String appId){
        // TODO（或者管理者处分析app状态，然后管理者按键操作）
        return applicationService.withdrawApplication(appId);
    }
//    返回该基金在该年度还剩余的额度
//    public SaResult getQuota(String expendNumber){
//
//    }
//    申请者-获取自己的申请记录（所有）
    @RequestMapping(value = "edit/getMyApps", method = RequestMethod.GET)
    @ResponseBody
    public SaResult getMyApps(){
        return applicationService.getMyApps(StpUtil.getLoginIdAsLong());
    }
//    获得自己关于某个基金的所有申请
    @RequestMapping(value = "edit/getMyAppsOfExpend", method = RequestMethod.GET)
    @ResponseBody
    public SaResult getMyAppsOfExpend(String expendNumber){
        return applicationService.getMyAppsOfExpend(expendNumber, StpUtil.getLoginIdAsLong());
    }
//    获得自己可以申请的基金
    @RequestMapping(value = "getAllMyExpends", method = RequestMethod.GET)
    @ResponseBody
    SaResult getAllMyExpends(){
        return applicationService.getAllMyExpends(StpUtil.getLoginIdAsLong());
    }

    /*
    TODO 管理者部分的api：
     1. 获取自己所有需要处理的申请（通过状态号筛选）
     2. 通过申请
     3. 拒绝申请
     */
    @RequestMapping(value = "edit/getMyAppsToExam", method = RequestMethod.GET)
    @ResponseBody
    public SaResult getMyAppsToExam(){
        return applicationService.getMyAppsToExam(StpUtil.getLoginIdAsLong());
    }

//    NOTE 组不重名
    @RequestMapping(value = "edit/passApplication", method = RequestMethod.POST)
    @ResponseBody
    public SaResult passApplication(String appId, String comment){
        return applicationService.passApplication(StpUtil.getLoginIdAsLong(), appId, comment);
    }

    @RequestMapping(value = "edit/rejectApplication", method = RequestMethod.POST)
    @ResponseBody
    public SaResult rejectApplication(String appId, String comment){
        return applicationService.rejectApplication(StpUtil.getLoginIdAsLong(), appId, comment);
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
                                              String expenditureTotalAmount, String beginTime, String endTime) throws ParseException {
        return applicationService.newExpenditureApplication(expenditureName, groupName, expenditureNumber,
                expenditureTotalAmount, beginTime, endTime, StpUtil.getLoginIdAsLong());
    }
    @RequestMapping(value ="edit/submitExpend", method= RequestMethod.POST)
    @ResponseBody
    public SaResult submitExpend(String expName, String expNumber, String totalAmound,
                                 String startTime, String endTime, String groupName) throws ParseException {
        return applicationService.submitExpend(expName, expNumber, totalAmound,
                startTime, endTime, groupName, StpUtil.getLoginIdAsLong());
    }


    /*
    TODO 管理者部分的api(Expend部分)：
     1. 获取自己所有需要处理的申请（通过状态号筛选）
     2. 通过申请
     3. 拒绝申请
     */
    @RequestMapping(value = "edit/getMyExpendsToExam", method = RequestMethod.GET)
    @ResponseBody
    public SaResult getMyExpendsToExam(){
        return applicationService.getMyExpendsToExam(StpUtil.getLoginIdAsLong());
    }

    //    NOTE 组不重名
    @RequestMapping(value = "edit/passExpenditure", method = RequestMethod.POST)
    @ResponseBody
    public SaResult passExpenditure(String appId){
        return applicationService.passExpenditure(StpUtil.getLoginIdAsLong(), appId);
    }

    @RequestMapping(value = "edit/rejectExpenditure", method = RequestMethod.POST)
    @ResponseBody
    public SaResult rejectExpenditure(String appId){
        return applicationService.rejectExpenditure(StpUtil.getLoginIdAsLong(), appId);
    }


    @RequestMapping(value ="file/uploadCsvFileToApply", method= RequestMethod.POST)
    @ResponseBody
    public SaResult uploadCsvFileToApply(MultipartFile uploadFile){
        // TODO
        return applicationService.uploadCsvFileToApply(uploadFile, StpUtil.getLoginIdAsLong());
    }

    @RequestMapping(value ="file/downloadCsvFileApplyFromOneExp", method= RequestMethod.GET)
    @ResponseBody
    public SaResult downloadCsvFileApplyFromOneExp(String expendNumber){
        // TODO
        return null;
    }
}
