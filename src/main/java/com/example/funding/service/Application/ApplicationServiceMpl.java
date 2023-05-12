package com.example.funding.service.Application;

import cn.dev33.satoken.util.SaResult;
import com.example.funding.bean.Application;
import com.example.funding.bean.Expenditure;
import com.example.funding.bean.Group;
import com.example.funding.bean.User;
import com.example.funding.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ApplicationServiceMpl implements ApplicationService{

    @Autowired
    ExpenditureDao expenditureDao;
    @Autowired
    UserDao userDao;
    @Autowired
    ApplicationDao applicationDao;
    @Autowired
    GroupDao groupDao;

    /**
     * AppInfo getAppInfoByNumber(String expendNumber, long staffId)
     * @param expendNumber
     * @param staffId
     * @return
     */
    @Override
    public AppInfo getAppInfoByNumber(String expendNumber, long staffId) {
        Expenditure expenditure = expenditureDao.findByNumber(expendNumber);
        if(expenditure == null){
            System.out.printf("this expenditure of %s is not exist\n", expendNumber);
            return null;
        }
        AppInfo appInfo = new AppInfo(expenditure);
        appInfo.setUserId(staffId);
        appInfo.setUserName(userDao.findById(staffId).get().getName());
        return appInfo;
    }

    /*
    失败的可能情况：
    1. 金额超过限定
    2.
    3. 将app加入到对应管理者的set里可能失败
     */
    @Override
    public long submitApplication(String expendNumber, int expendCategory,String abstrac , String comment, double amount, long userId) {
        Expenditure expenditure = expenditureDao.findByNumber(expendNumber);
        if(expenditure == null){
            System.out.printf("this expenditure of %s is not exist\n", expendNumber);
            return -1;
        }
        if(amount > expenditure.getRemainingAmount()){
            System.out.println("the amount requested exceeds the limit ");
            return -1;
        }
        Application application = new Application();
        application.setApplyTime(new Date());
        application.setApp_abstract(abstrac);
        application.setComment(comment);
        application.setUser(userDao.findById(userId).get());
        application.setExpenditure(expenditure);
        application.setStatus(0);
        application.setType(1);
        application.setAmount(amount);
        application.setExpendCategory(expendCategory);
        Application application1 = applicationDao.save(application);

        Group group = expenditure.getGroup();
        group.getUsers().stream().filter(s->s.getIdentity()>0).forEach(s->s.getAppToExam().add(application1));

        expenditureDao.updateRemainingAmountByNumber(expenditure.getRemainingAmount()-amount, expendNumber);

        return application1.getId();
    }

    @Override
    public boolean withdrawApplication(long appId) {
        Optional<Application> application = applicationDao.findById(appId);
        if(application.isEmpty()){
            System.out.println("this app is not present");
            return false;
        }
//        设置成撤销状态
        application.get().setStatus(3);
        return true;
    }
    /*
    获取自己提交的所有申请
     */
    public SaResult getMyApps(long userId){
        if(userDao.findById(userId).isEmpty()){
            return SaResult.error("the user is not exist");
        }
        List<AppInfo> appInfos = userDao.findById(userId).get().getApplications().stream()
                .sorted(Comparator.comparing(Application::getStatus).thenComparing(Application::getApplyTime)).map(AppInfo::new).toList();
        return SaResult.data(appInfos);
    }

    /*
    获得自己需要审批的app
    1. 遍历自己的appToExam，需要保留一个审批记录，所以返回的排序先按照状态号，再按照申请时间
     */
    @Override
    public SaResult getMyAppsToExam(long userId) {
        if(userDao.findById(userId).isEmpty()){
            return SaResult.error("the user is not exist");
        }
//        这里获得的app应该都是填好了全部的（有expend
        List<AppInfo> appInfos = userDao.findById(userId).get().getAppToExam().stream()
                .sorted(Comparator.comparing(Application::getStatus).thenComparing(Application::getApplyTime)).map(AppInfo::new).toList();
        return SaResult.data(appInfos);
    }

    /*
    1. 判断这个user是否在app所在的组里
    2. TODO 获得对象之后直接处理是否可以反映到数据库-不可以
     */
    @Override
    public SaResult passApplication(long userId, long appId) {
        Optional<Application> application = applicationDao.findById(appId);
        if(application.isEmpty()){
            return SaResult.error("this appId "+ appId + " is not exist");
        }
        Optional<User> user = userDao.findById(userId);
        if(user.isEmpty()){
            return SaResult.error("this userId "+ userId + " is not exist");
        }
        if(application.get().getExpenditure().getGroup().getUsers().contains(user.get())){
            applicationDao.updateStatusById(1, application.get().getId());
            return SaResult.ok("pass");
        }else{
            return SaResult.error("this user can not pass the application");
        }
    }

    @Override
    public SaResult rejectApplication(long userId, long appId) {
        Optional<Application> application = applicationDao.findById(appId);
        if(application.isEmpty()){
            return SaResult.error("this appId "+ appId + " is not exist");
        }
        Optional<User> user = userDao.findById(userId);
        if(user.isEmpty()){
            return SaResult.error("this userId "+ userId + " is not exist");
        }
        if(application.get().getExpenditure().getGroup().getUsers().contains(user.get())){
            applicationDao.updateStatusById(2, application.get().getId());
            return SaResult.ok("reject");
        }else{
            return SaResult.error("this user can not reject the application");
        }
    }

    /*
    1. 获得的是自己需要审批的所有基金申请
    2. 通过申请之后，该基金正式建立，加入小组等
    3. 拒绝之后没有具体变化
     */
    public SaResult getMyExpendsToExam(long userId){
        if(userDao.findById(userId).isEmpty()){
            return SaResult.error("the user is not exist");
        }
        /*
        TODO 1. 利用AppInfo类，同时申请的信息也会插入数据库，但是只有标志位为1才能被组选来申请application
        下面这句表示没被处理的放前面，相同处理状态的按照时间排序
         */
        List<AppInfo> appInfos = userDao.findById(userId).get().getAppToExam().stream()
                .sorted(Comparator.comparing(Application::getStatus).thenComparing(Application::getApplyTime)).map(AppInfo::new).toList();
        return SaResult.data(appInfos);
    }
    public SaResult passExpenditure(long userId, long expId){
        return null;
    }
    public SaResult rejectExpenditure(long userId, long expId){
        return null;
    }


    /*
        建立新的expend，填写具体的expend 名称
        报错：
        1. 填写的组别不属于该用户属于的组
        2. 填写的时间不对
        3. 该经费不存在（这个交给上面去审核）
     */
    public long newExpenditureApplication(String expenditureName, String groupName, String expenditureNumber,
                                          double expenditureTotalAmount, String beginTime, String endTime, long userId) throws ParseException {
        Expenditure expenditure = new Expenditure();
        expenditure.setName(expenditureName);
        expenditure.setNumber(expenditureNumber);
        expenditure.setTotalAmount(expenditureTotalAmount);
        expenditure.setRemainingAmount(expenditureTotalAmount);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date start = sdf.parse(beginTime), end = sdf.parse(beginTime);
        if(start.after(end)){
            System.out.println("begin time is after end");
            return -1;
        }
        expenditure.setStartTime(start);
        expenditure.setEndTime(end);
        Group group = groupDao.findByName(groupName);
        if(group == null){
            System.out.printf("this group %s is not exist\n", groupName);
            return -1;
        }
        Optional<User> user = userDao.findById(userId);
        if (user.isEmpty()){
            System.out.printf("this user %d is not exist\n",userId);
            return -1;
        }
        if (!user.get().getGroups().contains(group)){
            System.out.printf("this user %d is not belong to this group %s", userId, groupName);
            return -1;
        }
        expenditure.setGroup(groupDao.findByName(groupName));
        expenditure.setApplications(new HashSet<>());
        expenditure.setQuota(0);

//        将这个申请交给管理者
        Expenditure expenditure1 = expenditureDao.save(expenditure);
        group.getUsers().stream().filter(s->s.getIdentity()>0).forEach(s->s.getExpenditures().add(expenditure1));
        return expenditure1.getId();

    }




}
