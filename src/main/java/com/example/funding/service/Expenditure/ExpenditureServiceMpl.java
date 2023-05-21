package com.example.funding.service.Expenditure;

import cn.dev33.satoken.util.SaResult;
import com.example.funding.bean.Application;
import com.example.funding.bean.Expenditure;
import com.example.funding.bean.Group;
import com.example.funding.bean.User;
import com.example.funding.dao.*;
import com.example.funding.service.Application.AppInfo;
import com.example.funding.service.User.UserService;
import org.apache.poi.sl.usermodel.ObjectMetaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ExpenditureServiceMpl implements ExpenditureService{
    @Autowired
    private ExpenditureDao expenditureDao;
    @Autowired
    private UserService userService;

    @Autowired UserDao userDao;

    @Autowired GroupDao groupDao;

    @Autowired ApplicationDao applicationDao;

    @Override
    public SaResult getOneExpenditureAllInfo(String expenditureNumber){
//        List<Expenditure> expenditures = expenditureDao.findByNumber(expenditureNumber);
        Expenditure expenditure = expenditureDao.findByNumberAndStatus(expenditureNumber, 1);
        if (Objects.isNull(expenditure)) {
            return SaResult.error("the expenditure is not exist");
        }
        User user = userService.getMe();
        // 如果经费所属的课题组user没有这个课题组则返回错误
        if(!groupDao.findAllByUsers(user).contains(expenditure.getGroup())){
            return SaResult.error("the expenditure do not belong to the user");
        }
        return SaResult.data(new ExpenditureInfo(expenditure));
    }

    @Override
    public SaResult updateExpenditureQuota(String expenditureNumber, String quota){
//        List<Expenditure> expenditures = expenditureDao.findByNumber(expenditureNumber);
        Expenditure expenditure = expenditureDao.findByNumberAndStatus(expenditureNumber, 1);
        if (Objects.isNull(expenditure)) {
            return SaResult.error("the expenditure is not exist");
        }
        User user = userService.getMe();
        // 如果经费所属的课题组user没有这个课题组则返回错误
        if(!groupDao.findAllByUsers(user).contains(expenditure.getGroup())){
            return SaResult.error("the expenditure do not belong to the user");
        }
        // 将String转化为double
        double quotaDouble = Double.parseDouble(quota);
        expenditure.setQuota(quotaDouble);
        expenditureDao.save(expenditure);
        return SaResult.ok("update success");
    }

    @Override
    public SaResult updateExpenditureEndTime(String expenditureNumber, String endTime){
//        List<Expenditure> expenditures = expenditureDao.findByNumber(expenditureNumber);
        Expenditure expenditure = expenditureDao.findByNumberAndStatus(expenditureNumber, 1);
        if (Objects.isNull(expenditure)) {
            return SaResult.error("the expenditure is not exist");
        }
        User user = userService.getMe();
        // 如果经费所属的课题组user没有这个课题组则返回错误
        if(!groupDao.findAllByUsers(user).contains(expenditure.getGroup())){
            return  SaResult.error("the expenditure do not belong to the user");
        }
        // 将String转化为Date
        Date date = new Date();
        try {
            date = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(endTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Date startDate = expenditure.getStartTime();
        if(date.before(startDate)){
            return SaResult.error("the endTime is before the startTime");
        }
        expenditure.setEndTime(date);
        expenditureDao.save(expenditure);
        return SaResult.ok("update success");
    }

    @Override
    public SaResult getAllExpenditureInfoInOneGroup(String groupName){
        Group group = groupDao.findByName(groupName);
        if (Objects.isNull(group)) {
            return SaResult.error("the group is not exist");
        }
        User user = userService.getMe();
        // 如果经费所属的课题组user没有这个课题组则返回错误
        if(!groupDao.findAllByUsers(user).contains(group)){
             return SaResult.error("the group do not belong to the user");
        }
        List<Expenditure> expenditureList = expenditureDao.findAllByGroup(group);
        List<ExpenditureInfo> expenditureInfoList = new ArrayList<>();
        for(Expenditure expenditure:expenditureList){
            if (expenditure.getStatus()==0||expenditure.getStatus()==2){
                continue;
            }
            expenditureInfoList.add(new ExpenditureInfo(expenditure));
        }
        return SaResult.data(expenditureInfoList);
    }

    @Override
    public SaResult getAllExpenditureInfo(){
        User user = userService.getMe();
        List<Group> groupList = groupDao.findAllByUsers(user);
        List<ExpenditureInfo> expenditureInfoList = new ArrayList<>();
        for(Group group:groupList){
            List<Expenditure> expenditureList = expenditureDao.findAllByGroup(group);
            for(Expenditure expenditure:expenditureList){
                if (expenditure.getStatus()==0||expenditure.getStatus()==2){
                    continue;
                }
                expenditureInfoList.add(new ExpenditureInfo(expenditure));
            }
        }
        return SaResult.data(expenditureInfoList);
    }

    @Override
    public SaResult getOneExpenditureAppAllInfoByMyself(String expenditureNumber){
//        List<Expenditure> expenditures = expenditureDao.findByNumber(expenditureNumber);
        Expenditure expenditure = expenditureDao.findByNumberAndStatus(expenditureNumber, 1);
        if (Objects.isNull(expenditure)) {
            return SaResult.error("the expenditure is not exist");
        }
        User user = userService.getMe();
        // 如果经费所属的课题组user没有这个课题组则返回错误
        if(!groupDao.findAllByUsers(user).contains(expenditure.getGroup())){
            return SaResult.error("the expenditure do not belong to the user");
        }
        List<Application> applicationList = applicationDao.findAllByExpenditure(expenditure);
        List<AppInfo> applicationInfoList = new ArrayList<>();
        for(Application application:applicationList){
            if (application.getUser().equals(user)){
                applicationInfoList.add(new AppInfo(application));
            }
        }
        return SaResult.data(applicationInfoList);
    }
}
