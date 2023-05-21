package com.example.funding.service.Application;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.json.JSONObject;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.example.funding.bean.*;
import com.example.funding.dao.*;
import com.example.funding.service.Group.GroupInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.DateUtils;
import org.thymeleaf.util.NumberUtils;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.regex.Pattern;

@Service
@Transactional
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

    public ExpendCategory getCategoryValueFromStrings(String cate1, String cate2){
        ExpendCategory expendCategory;
        switch (cate1){
            case "Office":{
                expendCategory = ExpendCategory.Office.valueOf(cate2);
                break;
            }
            case "Print":{
                expendCategory = ExpendCategory.Print.valueOf(cate2);
                break;
            }
            case "Maintenance":{
                expendCategory = ExpendCategory.Maintenance.valueOf(cate2);
                break;
            }
            case "Postage":{
                expendCategory = ExpendCategory.Postage.valueOf(cate2);
                break;
            }
            case "Train":{
                expendCategory = ExpendCategory.Train.valueOf(cate2);
                break;
            }
            default:expendCategory = ExpendCategory.Error.noSuchCategory;
        }
        return expendCategory;
    }

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
//        List<Expenditure> expenditures = expenditureDao.findByNumber(expendNumber);
        Expenditure expenditure = expenditureDao.findByNumberAndStatus(expendNumber, 1);
        if(expenditure == null){
            return SaResult.error(String.format("this expenditure of %s is not exist\n", expendNumber));
        }
        AppInfo appInfo = new AppInfo(expenditure);
        appInfo.setUserId(staffId);
        appInfo.setUserName(userDao.findByUserId(staffId).getName());
        return SaResult.ok().setData(appInfo);
    }

    /*
    失败的可能情况：
    1. 金额超过限定
    2.
    3. 将app加入到对应管理者的set里可能失败
     */
    @Override
    public SaResult submitApplication(String expendNumber, String expendCategory1, String expendCategory2,
                                      String abstrac , String comment, String amount, long userId) {
        return  saveApplication(expendNumber, expendCategory1, expendCategory2,
                abstrac , comment, amount, userId, 0);
    }

    public SaResult tempSaveApplication(String expendNumber, String expendCategory1, String expendCategory2, String abstrac ,
                                 String comment, String amount, long userId){
        return saveApplication(expendNumber, expendCategory1, expendCategory2,
                abstrac , comment, amount, userId, 4);
    }
    public SaResult getTempSaveApp(Long userId){
        if (userId == null){
            return SaResult.error("userId is null");
        }
        List<Application> applications = applicationDao.findByUser_IdAndStatus(userId, 4);
        if (applications.size() == 0){
            return SaResult.error("no temp save");
        }
        Application application = new Application();
        application.setComment("no app find");
        return SaResult.ok().setData(new AppInfo(applications.stream()
                .max(Comparator.comparing(Application::getCreatedDate)).orElse(application)));

    }
    private SaResult saveApplication(String expendNumber, String expendCategory1, String expendCategory2,
                                     String abstrac , String comment, String amount, long userId, int saveStatus) {
//        List<Expenditure> expenditures = expenditureDao.findByNumber(expendNumber);
        Expenditure expenditure = expenditureDao.findByNumberAndStatus(expendNumber, 1);
        if(expenditure == null){
            return SaResult.error(String.format("this expenditure of %s is not exist\n", expendNumber));
        }
        ExpendCategory expCategory =getCategoryValueFromStrings(expendCategory1, expendCategory2);
        int expCate = expCategory.getValueOfExpend(expCategory);
        double amt = 0.0;
//        if(isInteger(expendCategory)){
//            expCate = Integer.parseInt(expendCategory);
//        }else{
//            return SaResult.error("this category is not integer");
//        }
        if (isDouble(amount)){
            amt = Double.parseDouble(amount);
        }else{
            return SaResult.error("this amount is not double");
        }
        if (amt + (Double)getQuota(expendNumber).getData() > expenditure.getQuota()){
            return SaResult.error("over quota");
        }
        if(amt > expenditure.getRemainingAmount()){
            return SaResult.error("the amount requested exceeds the limit ");
        }
        if (expenditure.getStatus() != 1){
            return SaResult.error(String.format("the status of expenditure is wrong; status: %d", expenditure.getStatus()));
        }
        User user = userDao.findByUserId(userId);
        if (user == null){
            return SaResult.error(String.format("user of %d is not exist", userId));
        }
        Application application = new Application();
        application.setCreatedDate(new Date());
        application.setApplyTime(new Date());
        application.setApp_abstract(abstrac);
        application.setComment(comment);
        application.setUser(user);
        application.setExpenditure(expenditure);
        application.setStatus(saveStatus);
        application.setType(1);
        application.setAmount(amt);
        application.setExpendCategory(expCate);
        Application application1 = applicationDao.save(application);

        Group group = expenditure.getGroup();
        if (saveStatus == 0){
            group.getUsers().stream().filter(s->s.getIdentity()>0).forEach(s->s.getAppToExam().add(application1));
            expenditureDao.updateRemainingAmountByNumber(expenditure.getRemainingAmount()-amt, expendNumber);
        }
        user.getApplications().add(application1);
//        group.getUsers().stream().forEach(s->userDao.save(s));

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
        if (application.get().getStatus() != 0){
            return SaResult.error("this application has been examined");
        }
//        设置成撤销状态
        Expenditure expenditure = application.get().getExpenditure();
        expenditureDao.updateRemainingAmountByNumber(
                application.get().getExpenditure().getRemainingAmount()+application.get().getAmount(),
                application.get().getExpenditure().getNumber());
        application.get().setStatus(3);
        return SaResult.ok();
    }
    /*
    TODO 获取该基金在这个时间段还有的余额，从基金申请开始一年为一个时间段
     */
    public java.util.Date getLocalDate(java.util.Date date1){
        java.util.Date date = new Date(date1.getTime());
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate localDate = instant.atZone(zoneId).toLocalDate();
        Date dateNow = new Date();
        instant = dateNow.toInstant();
        LocalDate localDateNow = instant.atZone(zoneId).toLocalDate();
        while (localDate.isBefore(localDateNow)){
            localDate = localDate.plusYears(1);
        }
        localDate = localDate.minusYears(1);
        ZonedDateTime zdt = localDate.atStartOfDay(zoneId);
        return Date.from(zdt.toInstant());
    }
    public SaResult getQuota(String expendNumber){
        Expenditure expenditure = expenditureDao.findByNumberAndStatus(expendNumber, 1);
        if (expenditure == null){
            return SaResult.error("wrong expendNumber, there is no such expenditure");
        }
        java.util.Date date = expenditure.getStartTime();
        date = getLocalDate(date);
        java.util.Date finalDate = date;
        List<Application> applications = expenditureDao
                .findByNumberAndStatus(expendNumber, 1)
                .getApplications().stream()
                .filter(s->s.getCreatedDate().after(finalDate) && s.getStatus()<=1)
                .toList();
        return SaResult.ok().setData(applications.stream().mapToDouble(Application::getAmount).sum());
    }
    /*
    获取自己提交的所有申请
     */
    public SaResult getMyApps(long userId){
        if(userDao.findByUserId(userId) == null){
            return SaResult.error("the user is not exist");
        }
        List<AppInfo> appInfos = userDao.findByUserId(userId).getApplications().stream()
                .sorted(Comparator.comparing(Application::getStatus).
                        thenComparing(Application::getCreatedDate).reversed()).map(AppInfo::new).toList();
        return SaResult.data(appInfos);
    }
    /*
    获取自己关于某个基金的所有申请
     */
    public SaResult getMyAppsOfExpend(String expendNumber, long userId){
        if(userDao.findByUserId(userId) == null){
            return SaResult.error("the user is not exist");
        }
        List<AppInfo> appInfos = userDao.findByUserId(userId).getApplications().stream()
                .filter(s->s.getExpenditure().getNumber().equals(expendNumber))
                .sorted(Comparator.comparing(Application::getStatus).thenComparing(Application::getCreatedDate)).map(AppInfo::new).toList();
        return SaResult.data(appInfos);
    }

    /*
    获得自己需要审批的app
    1. 遍历自己的appToExam，需要保留一个审批记录，所以返回的排序先按照状态号，再按照申请时间
     */
    @Override
    public SaResult getMyAppsToExam(long userId) {
        if(userDao.findByUserId(userId) == null){
            return SaResult.error("the user is not exist");
        }
//        这里获得的app应该都是填好了全部的（有expend
//        List<AppInfo> appInfos = userDao.findById(userId).get().getAppToExam().stream()
//                .sorted(Comparator.comparing(Application::getStatus).thenComparing(Application::getCreatedDate)).map(AppInfo::new).toList();
        List<AppInfo> appInfos = userDao.findByUserId(userId).getAppToExam().stream()
                .filter(s->s.getStatus() == 0).map(AppInfo::new).toList();
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
        User user = userDao.findByUserId(userId);
        if(user == null){
            return SaResult.error("this userId "+ userId + " is not exist");
        }
//        判断application是否可以被修改
        if (application.get().getStatus() != 0){
            return SaResult.error("this application can not be modified");
        }
//        检查这个人是否有权限修改
        if(application.get().getExpenditure().getGroup().getUsers().stream()
                .map(s->s.getEmail()+s.getIdentity()).toList()
                .contains(user.getEmail()+user.getIdentity())){
            applicationDao.updateStatusById(1, application.get().getId());
            expenditureDao.updateRemainingAmountByNumber(
                    application.get().getExpenditure().getRemainingAmount()+application.get().getAmount(),
                    application.get().getExpenditure().getNumber());
            Feedback feedback = new Feedback();
            feedback.setComment(comment);
            feedback.setReplyTime(new Date());
            feedback.setCreatedDate(new Date());
            feedback.setUser(user);
            feedback.setApplicationId(application.get().getId());
            feedback.setRead(0);
            feedbackDao.save(feedback);
//          申请者会收到feedback
            application.get().getUser().getFeedbacks().add(feedback);
            userDao.save(application.get().getUser());
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
        User user = userDao.findByUserId(userId);
        if(user == null){
            return SaResult.error("this userId "+ userId + " is not exist");
        }
        if (application.get().getStatus() != 0){
            return SaResult.error("this application can not be modified");
        }
        if(application.get().getExpenditure().getGroup().getUsers().stream()
                .map(s->s.getEmail()+s.getIdentity()).toList()
                .contains(user.getEmail()+user.getIdentity())){
            applicationDao.updateStatusById(2, application.get().getId());
            Feedback feedback = new Feedback();
            feedback.setComment(comment);
            feedback.setReplyTime(new Date());
            feedback.setCreatedDate(new Date());
            feedback.setUser(user);
            feedback.setApplicationId(application.get().getId());
            feedback.setRead(0);
            feedbackDao.save(feedback);
//          申请者会收到feedback
            application.get().getUser().getFeedbacks().add(feedback);
            userDao.save(application.get().getUser());
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
        if(userDao.findByUserId(userId) == null){
            return SaResult.error("the user is not exist");
        }
        /*
        利用AppInfo类，同时申请的信息也会插入数据库，但是只有标志位为1才能被组选来申请application
        下面这句表示没被处理的放前面，相同处理状态的按照时间排序
         */
//        List<ExpendInfo> expInfos = userDao.findById(userId).get().getExpendToExam().stream()
//                .sorted(Comparator.comparing(Expenditure::getStatus).thenComparing(Expenditure::getCreatedDate)).map(ExpendInfo::new).toList();
        List<ExpendInfo> expInfos = userDao.findByUserId(userId).getExpendToExam().stream()
                .filter(s->s.getStatus()==0).map(ExpendInfo::new).toList();
        return SaResult.data(expInfos);
    }
//    TODO quota是管理员设置？
    public SaResult submitExpend(String expName, String expNumber, String totalAmound,
                                 String startTime, String endTime, String groupName, long userId) throws ParseException {
        return saveExpend(expName, expNumber, totalAmound,
                startTime, endTime, groupName, userId, 0);
        }
        public SaResult tempSaveExpend(String expName, String expNumber, String totalAmound,
                                       String startTime, String endTime, String groupName, long userId) throws ParseException {
            return saveExpend(expName, expNumber, totalAmound,
                    startTime, endTime, groupName, userId, 4);
        }
    private SaResult saveExpend(String expName, String expNumber, String totalAmound,
                                String startTime, String endTime, String groupName, long userId, int saveStatus) throws ParseException {
        /*
        验证各种关系：是否存在这个人/小组，小组包含人？小组已有该基金？
        检验小信息：时间（没有很严格）
         */
//        当前用户与申请小组的检测
        User user = userDao.findByUserId(userId);
        if (user == null) {
            return SaResult.error("this user is not exist");
        }
        Group group = groupDao.findByName(groupName);
        if (group == null) {
            return SaResult.error("this group is not exist");
        }
        if (!group.getUsers().stream().map(s -> s.getEmail() + s.getIdentity()).toList()
                .contains(user.getEmail() + user.getIdentity())) {
            return SaResult.error("this user cannot submit this expenditure application for this group");
        }
//        number存在
        Optional<Expenditure> e0 = group.getExpenditures().stream().filter(s -> s.getNumber().equals(expNumber))
                .findFirst();
        if (e0.isPresent()) {
            return SaResult.error("this expenditure number has been exist");
        }
//      检测amount是否
        double amt = 0.0;
        if (isDouble(totalAmound)) {
            amt = Double.parseDouble(totalAmound);
        } else {
            return SaResult.error("this amount is not double");
        }
        if (amt < 0) {
            return SaResult.error("the amount is illegal ");
        }
//        时间的简单检测
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date start = sdf.parse(startTime), end = sdf.parse(endTime);
        if (start.after(end)) {
            return SaResult.error("the start time is after end");
        }
//        检查是否已存在
        if (group.getExpenditures().stream().map(Expenditure::getNumber).toList().contains(expNumber)) {
            return SaResult.error("This expenditure has been exist");
        }
//      提交与保存
        Expenditure e = new Expenditure();
        e.setName(expName);
        e.setNumber(expNumber);
        e.setTotalAmount(amt);
        e.setRemainingAmount(amt);
        e.setCreatedDate(new Date());
        e.setApplications(new HashSet<>());
        e.setStartTime(start);
        e.setEndTime(end);
        e.setGroup(group);
        e.setQuota(amt);
        e.setStatus(saveStatus);
        e.setType(0);
        Expenditure expenditure = expenditureDao.save(e);
        if (saveStatus==0) {
            group.getUsers().stream().filter(s -> s.getIdentity() > 0).forEach(s -> {
                s.getExpendToExam().add(expenditure);
//            userDao.save(s);
            });
        }
        ExpendInfo expendInfo = new ExpendInfo(expenditure);
        userDao.updateSexById(user.getSex(), user.getId());
        return SaResult.ok().setData(expendInfo);
    }
    public SaResult checkUserAndExpend(User user, Expenditure expenditure){
        if (user == null){
            return SaResult.error("this user is not exist");
        }
        if (expenditure == null){
            return SaResult.error("this expenditure is not exist");
        }
        if (user.getIdentity()==0){
            return SaResult.error("this person has no right to pass");
        }
        if (!expenditure.getGroup().getUsers().contains(user)){
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
        User user = userDao.findByUserId(userId);
        Expenditure expenditure = expenditureDao.findById(expID);
        if (expenditure.getStatus() != 0){
            return SaResult.error("this expenditure can not be modified");
        }
        if (expenditureDao.existsByNumberAndStatus(expenditure.getNumber(), 1)){
            expenditureDao.updateStatusById(2, expenditure.getId());
            return SaResult.error("this expenditure has been set up");
        }
        SaResult res = checkUserAndExpend(user, expenditure);
        if (res.getCode()==200){
            int expenditure1 = expenditureDao.updateStatusById(1, expID);
            expenditure.getGroup().getExpenditures().add(expenditure);
            expenditureDao.save(expenditure);
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
        User user = userDao.findByUserId(userId);
        Expenditure expenditure = expenditureDao.findById(expID);
        if (expenditure.getStatus() != 0){
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
        User user = userDao.findByUserId(userId);
        if (user == null){
            return SaResult.error(String.format("this user %d is not exist\n",userId)).setData(-1);
        }
        if (!groupInfo.getMemberNames().contains(user.getName())){
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
        User user = userDao.findByUserId(userId);
        if (user == null){
            return SaResult.error("this user is not exist");
        }
        List<ExpendInfo> expendInfos = new ArrayList<>();
        user.getGroups().forEach(s->s.getExpenditures().forEach(m->expendInfos.add(new ExpendInfo(m))));
        Set<Group> groups = user.getGroups();
        groups.stream().forEach(s->{
//            System.out.println(s.getName());
            s.getExpenditures().stream().forEach(m-> System.out.println(m.getName()));
//            System.out.println("finished "+ s.getName());
        });

        return SaResult.ok().setData(expendInfos);
    }


    /*
    csv文件提出申请
     */

    public SaResult uploadCsvFileToApply(MultipartFile file, long userId){
        User user = userDao.findByUserId(userId);
        if (user==null){
            return SaResult.error("this user is not exist");
        }
        if (file.isEmpty()){
            return SaResult.error("this file is empty");
        }
        String fileName = file.getOriginalFilename();
        String suffixName = null;
        if (fileName != null) {
            suffixName = fileName.substring(fileName.lastIndexOf("."));
        }
        if (suffixName != null && !suffixName.equals(".xlsx")) {
            return SaResult.error("this file is not xlsx");
        }
        InputStream inputStream = null;
        List<AppExcel> appExcels = null;
        try {
            inputStream = file.getInputStream();
            // 使用输入流进行操作
            appExcels = EasyExcel.read(inputStream).head(AppExcel.class).sheet(0).doReadSync();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            // 处理文件不存在的情况
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    // 处理关闭输入流时的异常
                }
            }
        }
        if (appExcels != null) {
            for (AppExcel appExcel : appExcels) {
                SaResult res = submitApplication(appExcel.getExpenditureId(), appExcel.getCategory1(),appExcel.getCategory2(),
                        appExcel.getAbstracts(), appExcel.getComment(), appExcel.getAmount(), StpUtil.getLoginIdAsLong());
                if (res.getCode() != 200) {
                    return res;
                }
                //按理说应该回退
            }
        }
        return SaResult.ok();
    }

    public SaResult downloadCsvFileApplyFromOneExp(HttpServletResponse response, String expenditureNumber, long userId){
        List<AppExcel> appExcels = CreateDataList();
        try{
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("经费明细表", StandardCharsets.UTF_8);
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            // 这里需要设置不关闭流
            EasyExcel.write(response.getOutputStream(), User.class).autoCloseStream(Boolean.FALSE).sheet("员工")
                    .doWrite(appExcels);
        }catch (Exception e) {
            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            Map<String, String> map = new HashMap<String, String>();
            map.put("status", "failure");
            map.put("message", "下载文件失败" + e.getMessage());
            JSONObject jsonObject=new JSONObject();;
            try {
                response.getWriter().println(jsonObject.toString());
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        return SaResult.ok();
    }

    private List<AppExcel> CreateDataList(){
        return null;
    }
}
