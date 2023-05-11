package com.example.funding.service.Application;

import com.example.funding.bean.Expenditure;
import com.example.funding.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApplicationServiceMpl implements ApplicationService{

    @Autowired
    ExpenditureDao expenditureDao;
    @Autowired
    UserDao userDao;
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
    public boolean submitApplication(AppInfo appInfo){
        return true;
    }
}
