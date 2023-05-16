package com.example.funding.service.Expenditure;

import com.example.funding.bean.Expenditure;
import com.example.funding.bean.Group;
import com.example.funding.bean.User;
import com.example.funding.dao.*;
import com.example.funding.service.User.UserService;
import com.example.funding.service.User.UserServiceMpl;
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

    @Override
    public ExpenditureInfo getOneExpenditureAllInfo(String expenditureNumber){
        Expenditure expenditure = expenditureDao.findByNumber(expenditureNumber);
        if (Objects.isNull(expenditure)) {
            throw new RuntimeException("the expenditure is not exist");
        }
        User user = userService.getMe();
        // 如果经费所属的课题组user没有这个课题组则返回错误
        if(!groupDao.findAllByUsers(user).contains(expenditure.getGroup())){
            throw new RuntimeException("the expenditure do not belong to the user");
        }
        return new ExpenditureInfo(expenditure);
    }

    @Override
    public boolean updateExpenditureQuota(String expenditureNumber, String quota){
        Expenditure expenditure = expenditureDao.findByNumber(expenditureNumber);
        if (Objects.isNull(expenditure)) {
            throw new RuntimeException("the expenditure is not exist");
        }
        User user = userService.getMe();
        // 如果经费所属的课题组user没有这个课题组则返回错误
        if(!groupDao.findAllByUsers(user).contains(expenditure.getGroup())){
            throw new RuntimeException("the expenditure do not belong to the user");
        }
        // 将String转化为double
        double quotaDouble = Double.parseDouble(quota);
        expenditure.setQuota(quotaDouble);
        expenditureDao.save(expenditure);
        return true;
    }

    @Override
    public boolean updateExpenditureEndTime(String expenditureNumber, String endTime){
        Expenditure expenditure = expenditureDao.findByNumber(expenditureNumber);
        if (Objects.isNull(expenditure)) {
            throw new RuntimeException("the expenditure is not exist");
        }
        User user = userService.getMe();
        // 如果经费所属的课题组user没有这个课题组则返回错误
        if(!groupDao.findAllByUsers(user).contains(expenditure.getGroup())){
            throw new RuntimeException("the expenditure do not belong to the user");
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
            throw new RuntimeException("the endTime is before the startTime");
        }
        expenditure.setEndTime(date);
        expenditureDao.save(expenditure);
        return true;
    }

    @Override
    public List<ExpenditureInfo> getAllExpenditureInfoInOneGroup(String groupName){
        Group group = groupDao.findByName(groupName);
        if (Objects.isNull(group)) {
            throw new RuntimeException("the group is not exist");
        }
        User user = userService.getMe();
        // 如果经费所属的课题组user没有这个课题组则返回错误
        if(!groupDao.findAllByUsers(user).contains(group)){
            throw new RuntimeException("the group do not belong to the user");
        }
        List<Expenditure> expenditureList = expenditureDao.findAllByGroup(group);
        List<ExpenditureInfo> expenditureInfoList = new ArrayList<>();
        for(Expenditure expenditure:expenditureList){
            expenditureInfoList.add(new ExpenditureInfo(expenditure));
        }
        return expenditureInfoList;
    }

    @Override
    public List<ExpenditureInfo> getAllExpenditureInfo(){
        User user = userService.getMe();
        List<Group> groupList = groupDao.findAllByUsers(user);
        List<ExpenditureInfo> expenditureInfoList = new ArrayList<>();
        for(Group group:groupList){
            List<Expenditure> expenditureList = expenditureDao.findAllByGroup(group);
            for(Expenditure expenditure:expenditureList){
                expenditureInfoList.add(new ExpenditureInfo(expenditure));
            }
        }
        return expenditureInfoList;
    }
}
