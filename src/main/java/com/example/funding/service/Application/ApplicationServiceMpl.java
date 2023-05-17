package com.example.funding.service.Application;

import cn.dev33.satoken.util.SaResult;
import com.example.funding.bean.*;
import com.example.funding.dao.*;
import com.example.funding.service.Group.GroupInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.DateUtils;
import org.thymeleaf.util.NumberUtils;
import org.thymeleaf.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;
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
    @Autowired
    FeedbackDao feedbackDao;

    public static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]+$");
        return pattern.matcher(str).matches();
    }

    public static boolean isDouble(String str) {
        if (str == null) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");
        return pattern.matcher(str).matches();
    }

    /**
     * AppInfo getAppInfoByNumber(String expendNumber, long staffId)
     * @param expendNumber
     * @param staffId
     * @return
     */
    @Override
    public SaResult getAppInfoByNumber(String expendNumber, long staffId) {
        Expenditure expenditure = expenditureDao.findByNumber(expendNumber);
        if(expenditure == null){
            return SaResult.error(String.format("this expenditure of %s is not exist\n", expendNumber));
        }
        AppInfo appInfo = new AppInfo(expenditure);
        appInfo.setUserId(staffId);
        appInfo.setUserName(userDao.findById(staffId).get().getName());
        return SaResult.ok().setData(appInfo);
    }

    /*
    失败的可能情况：
    1. 金额超过限定
    2.
    3. 将app加入到对应管理者的set里可能失败
     */
    @Override
    public SaResult submitApplication(String expendNumber, String expendCategory,String abstrac , String comment, String amount, long userId) {
        Expenditure expenditure = expenditureDao.findByNumber(expendNumber);
        if(expenditure == null){
            return SaResult.error(String.format("this expenditure of %s is not exist\n", expendNumber));
        }
        int expCate = 0;
        double amt = 0.0;
        if(isInteger(expendCategory)){
            expCate = Integer.parseInt(expendCategory);
        }else{
            return SaResult.error("this category is not integer");
        }
        if (isDouble(amount)){
            amt = Double.parseDouble(amount);
        }else{
            return SaResult.error("this amount is not double");
        }
        if(amt > expenditure.getRemainingAmount()){
            return SaResult.error("the amount requested exceeds the limit ");
        }
        if (expenditure.getStatus() != 1){
            return SaResult.error(String.format("the status is wrong; status: %d", expenditure.getStatus()));
        }
        Optional<User> user = userDao.findById(userId);
        if (user.isEmpty()){
            return SaResult.error(String.format("user of %d is not exist", userId));
        }
        User user1 = user.get();
        Application application = new Application();
        application.setCreatedDate(new Date());
        application.setApplyTime(new Date());
        application.setApp_abstract(abstrac);
        application.setComment(comment);
        application.setUser(user1);
        application.setExpenditure(expenditure);
        application.setStatus(0);
        application.setType(1);
        application.setAmount(amt);
        application.setExpendCategory(expCate);
        Application application1 = applicationDao.save(application);

        Group group = expenditure.getGroup();
        group.getUsers().stream().filter(s->s.getIdentity()>0).forEach(s->s.getAppToExam().add(application1));
        user1.getApplications().add(application1);
//        group.getUsers().stream().forEach(s->userDao.save(s));

        expenditureDao.updateRemainingAmountByNumber(expenditure.getRemainingAmount()-amt, expendNumber);

        return SaResult.ok().setData(application1.getId());
    }

    @Override
    public SaResult withdrawApplication(String appId) {
        long appID = 0;
        if (isInteger(appId)){
            appID = Integer.parseInt(appId);
        }else{
            return SaResult.error("this id is not int");
        }
        Optional<Application> application = applicationDao.findById(appID);
        if(application.isEmpty()){
            return SaResult.error("this app is not present");
        }
//        设置成撤销状态
        application.get().setStatus(3);
        return SaResult.ok();
    }
    /*
    TODO 获取该基金在这个时间段还有的余额，从基金申请开始一年为一个时间段
     */
    public SaResult getQuota(String expendNumber){
        return SaResult.error("haven't finished");
    }
    /*
    获取自己提交的所有申请
     */
    public SaResult getMyApps(long userId){
        if(userDao.findById(userId).isEmpty()){
            return SaResult.error("the user is not exist");
        }
        List<AppInfo> appInfos = userDao.findById(userId).get().getApplications().stream()
                .sorted(Comparator.comparing(Application::getStatus).thenComparing(Application::getCreatedDate)).map(AppInfo::new).toList();
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
                .sorted(Comparator.comparing(Application::getStatus).thenComparing(Application::getCreatedDate)).map(AppInfo::new).toList();
        return SaResult.data(appInfos);
    }

    /*
    1. 判断这个user是否在app所在的组里
    2. TODO 获得对象之后直接处理是否可以反映到数据库-不可以
     */
    @Override
    public SaResult passApplication(long userId, String appId, String comment) {
        long appID = 0;
//        检查appID是否为整数
        if (isInteger(appId)){
            appID = Integer.parseInt(appId);
        }else{
            return SaResult.error("this id is not int");
        }
//        application不为空,
        Optional<Application> application = applicationDao.findById(appID);
        if(application.isEmpty()){
            return SaResult.error("this appId "+ appId + " is not exist");
        }
//        user不为空
        Optional<User> user = userDao.findById(userId);
        if(user.isEmpty()){
            return SaResult.error("this userId "+ userId + " is not exist");
        }
//        判断application是否可以被修改
        if (application.get().getStatus() != 0){
            return SaResult.error("this application can not be modified");
        }
//        检查这个人是否有权限修改
        if(application.get().getExpenditure().getGroup().getUsers().stream()
                .map(s->s.getEmail()+s.getIdentity()).toList()
                .contains(user.get().getEmail()+user.get().getIdentity())){
            applicationDao.updateStatusById(1, application.get().getId());
            Feedback feedback = new Feedback();
            feedback.setComment(comment);
            feedback.setReplyTime(new Date());
            feedback.setCreatedDate(new Date());
            feedback.setUser(user.get());
            feedback.setApplicationId(application.get().getId());
            feedback.setRead(0);
            feedbackDao.save(feedback);
//          申请者会收到feedback
            application.get().getUser().getFeedbacks().add(feedback);

            return SaResult.ok("pass");
        }else{
            return SaResult.error("this user can not pass the application");
        }
    }

    @Override
    public SaResult rejectApplication(long userId, String appId, String comment) {
        long appID = 0;
        if (isInteger(appId)){
            appID = Integer.parseInt(appId);
        }else{
            return SaResult.error("this id is not int");
        }
        Optional<Application> application = applicationDao.findById(appID);
        if(application.isEmpty()){
            return SaResult.error("this appId "+ appId + " is not exist");
        }
        Optional<User> user = userDao.findById(userId);
        if(user.isEmpty()){
            return SaResult.error("this userId "+ userId + " is not exist");
        }
        if (application.get().getStatus() != 0){
            return SaResult.error("this application can not be modified");
        }
        if(application.get().getExpenditure().getGroup().getUsers().stream()
                .map(s->s.getEmail()+s.getIdentity()).toList()
                .contains(user.get().getEmail()+user.get().getIdentity())){
            applicationDao.updateStatusById(2, application.get().getId());
            Feedback feedback = new Feedback();
            feedback.setComment(comment);
            feedback.setReplyTime(new Date());
            feedback.setCreatedDate(new Date());
            feedback.setUser(user.get());
            feedback.setApplicationId(application.get().getId());
            feedback.setRead(0);
            feedbackDao.save(feedback);
//          申请者会收到feedback
            application.get().getUser().getFeedbacks().add(feedback);
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
        利用AppInfo类，同时申请的信息也会插入数据库，但是只有标志位为1才能被组选来申请application
        下面这句表示没被处理的放前面，相同处理状态的按照时间排序
         */
        List<ExpendInfo> expInfos = userDao.findById(userId).get().getExpendToExam().stream()
                .sorted(Comparator.comparing(Expenditure::getStatus).thenComparing(Expenditure::getCreatedDate)).map(ExpendInfo::new).toList();
        return SaResult.data(expInfos);
    }
//    TODO quota是管理员设置？
    public SaResult submitExpend(String expName, String expNumber, String totalAmound,
                                 String startTime, String endTime, String groupName, long userId) throws ParseException {
        /*
        验证各种关系：是否存在这个人/小组，小组包含人？小组已有该基金？
        检验小信息：时间（没有很严格）
         */
//        当前用户与申请小组的检测
        Optional<User> user = userDao.findById(userId);
        if (user.isEmpty()){
            return SaResult.error("this user is not exist");
        }
        Group group = groupDao.findByName(groupName);
        if(group == null){
            return SaResult.error("this group is not exist");
        }
        if (!group.getUsers().stream().map(s->s.getEmail()+s.getIdentity()).toList()
                .contains(user.get().getEmail()+user.get().getIdentity())){
            return SaResult.error("this user cannot submit this expenditure application for this group");
        }
//        number存在
        Optional<Expenditure> e0 = group.getExpenditures().stream().filter(s->s.getNumber().equals(expNumber))
                .findFirst();
        if(e0.isPresent()) {
            return SaResult.error("this expenditure number has been exist");
        }
//      检测amount是否
        double amt = 0.0;
        if (isDouble(totalAmound)){
            amt = Double.parseDouble(totalAmound);
        }else{
            return SaResult.error("this amount is not double");
        }
        if(amt < 0){
            return SaResult.error("the amount is illegal ");
        }
//        时间的简单检测
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date start = sdf.parse(startTime), end = sdf.parse(endTime);
        if (start.after(end)){
            return SaResult.error("the start time is after end");
        }
//        检查是否已存在
        if (group.getExpenditures().stream().map(Expenditure::getNumber).toList().contains(expNumber)){
            return SaResult.error("This expenditure has been exist");
        }
//      提交与保存
        Expenditure e = new Expenditure();
        e.setName(expName);
        e.setNumber(expNumber);
        e.setTotalAmount(amt);
        e.setRemainingAmount(amt);
        e.setCreatedDate(new Date());
        e.setStartTime(start);
        e.setEndTime(end);
        e.setGroup(group);
        e.setQuota(amt);
        e.setStatus(0);
        e.setType(0);
        Expenditure expenditure = expenditureDao.save(e);
        group.getUsers().stream().filter(s->s.getIdentity()>0).forEach(s->{
            s.getExpendToExam().add(expenditure);
//            userDao.save(s);
        });
        ExpendInfo expendInfo = new ExpendInfo(expenditure);
        userDao.updateSexById(user.get().getSex(), user.get().getId());
        return SaResult.ok().setData(expendInfo);
    }
    public SaResult checkUserAndExpend(Optional<User> user, Optional<Expenditure> expenditure){
        if (user.isEmpty()){
            return SaResult.error("this user is not exist");
        }
        if (expenditure.isEmpty()){
            return SaResult.error("this expenditure is not exist");
        }
        if (user.get().getIdentity()==0){
            return SaResult.error("this person has no right to pass");
        }
        if (!expenditure.get().getGroup().getUsers().contains(user.get())){
            return SaResult.error("this person not in the group of the expenditure");
        }
        return SaResult.ok();
    }
    public SaResult passExpenditure(long userId, String expId){
        long expID = 0;
        if (isInteger(expId)){
            expID = Integer.parseInt(expId);
        }else{
            return SaResult.error("this expenditure ID is not integer");
        }
        Optional<User> user = userDao.findById(userId);
        Optional<Expenditure> expenditure = expenditureDao.findById(expID);
        if (expenditure.get().getStatus() != 0){
            return SaResult.error("this expenditure can not be modified");
        }
        SaResult res = checkUserAndExpend(user, expenditure);
        if (res.getCode()==200){
            int expenditure1 = expenditureDao.updateStatusById(1, expID);
            expenditure.get().getGroup().getExpenditures().add(expenditure.get());
            expenditureDao.save(expenditure.get());
            return SaResult.ok().setData(expenditure1);
        }else return res;
    }
    public SaResult rejectExpenditure(long userId, String expId){
        long expID = 0;
        if (isInteger(expId)){
            expID = Integer.parseInt(expId);
        }else{
            return SaResult.error("this expenditure ID is not integer");
        }
        Optional<User> user = userDao.findById(userId);
        Optional<Expenditure> expenditure = expenditureDao.findById(expID);
        if (expenditure.get().getStatus() != 0){
            return SaResult.error("this expenditure can not be modified");
        }
        SaResult res = checkUserAndExpend(user, expenditure);
        if (res.getCode()==200){
            int expenditure1 = expenditureDao.updateStatusById(2, expID);
            return SaResult.ok().setData(expenditure1);
        }else return res;
    }


    /*
        建立新的expend，填写具体的expend 名称
        报错：
        1. 填写的组别不属于该用户属于的组
        2. 填写的时间不对
        3. 该经费不存在（这个交给上面去审核）

        TODO 这里是直接创建，之后写一个申请的方法，申请通过后才正式创建
     */
    public SaResult newExpenditureApplication(String expenditureName, String groupName, String expenditureNumber,
                                          String expenditureTotalAmount, String beginTime, String endTime, long userId) throws ParseException {
        double expTotalAmt = 0;
        if (isDouble(expenditureTotalAmount)){
            expTotalAmt = Integer.parseInt(expenditureTotalAmount);
        }else{
            return SaResult.error("this total amount is not double");
        }
        Expenditure expenditure = new Expenditure();
        expenditure.setName(expenditureName);
        expenditure.setNumber(expenditureNumber);
        expenditure.setTotalAmount(expTotalAmt);
        expenditure.setRemainingAmount(expTotalAmt);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date start = sdf.parse(beginTime), end = sdf.parse(beginTime);
        if(start.after(end)){
            return SaResult.error("begin time is after end").setData(-1);
        }
        expenditure.setStartTime(start);
        expenditure.setEndTime(end);
        Group group = groupDao.findByName(groupName);
        if(group == null){
            return SaResult.error(String.format("this group %s is not exist\n", groupName)).setData(-1);
        }
        GroupInfo groupInfo = new GroupInfo(group);
        Optional<User> user = userDao.findById(userId);
        if (user.isEmpty()){
            return SaResult.error(String.format("this user %d is not exist\n",userId)).setData(-1);
        }
        if (!groupInfo.getMemberNames().contains(user.get().getName())){
//        if (!group.getUsers().contains(user.get())){
            return SaResult.error(String.format("this user %d is not belong to this group %s", userId, groupName)).setData(-1);
        }
        expenditure.setGroup(groupDao.findByName(groupName));
        expenditure.setApplications(new HashSet<>());
        expenditure.setQuota(0);

//        将这个申请交给管理者
        Expenditure expenditure1 = expenditureDao.save(expenditure);
        group.getUsers().stream().filter(s->s.getIdentity()>0).forEach(s->s.getExpenditures().add(expenditure1));
        return SaResult.ok("success!").setData(expenditure1.getId());

    }

    /*
    获得这个用户所有可以申请的基金
     */
    public SaResult getAllMyExpends(long userId){
        Optional<User> user = userDao.findById(userId);
        if (user.isEmpty()){
            return SaResult.error("this user is not exist");
        }
        List<ExpendInfo> expendInfos = new ArrayList<>();
        user.get().getGroups().forEach(s->s.getExpenditures().forEach(m->expendInfos.add(new ExpendInfo(m))));
        Set<Group> groups = user.get().getGroups();
        groups.stream().forEach(s->{
//            System.out.println(s.getName());
            s.getExpenditures().stream().forEach(m-> System.out.println(m.getName()));
//            System.out.println("finished "+ s.getName());
        });

        return SaResult.ok().setData(expendInfos);
    }



}
