package com.example.funding.service.Application;

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
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;

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
        appInfo.setUseId(staffId);
        appInfo.setUserName(userDao.findById(staffId).get().getName());
        return appInfo;
    }

    /*
    失败的可能情况：
    1. 金额超过限定
    2. TODO 这个save过程怎么判断是否成功？
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
        group.getUsers().stream().filter(s->s.getIdentity()>0).forEach(s->s.getApplications().add(application1));

        expenditure.setRemainingAmount(expenditure.getRemainingAmount()-amount);

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
