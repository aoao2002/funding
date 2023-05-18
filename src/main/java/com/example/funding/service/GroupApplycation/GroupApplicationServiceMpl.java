package com.example.funding.service.GroupApplycation;

import com.example.funding.Util.Exception.BeanException;
import com.example.funding.bean.*;
import com.example.funding.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class GroupApplicationServiceMpl implements GroupApplicationService{
    @Autowired
    private GroupApplicationDao groupApplicationDao;
    @Autowired
    private UserDao userDao;

    @Override
    public GroupApplication findById(long applyID) {
        Optional<GroupApplication> us = groupApplicationDao.findById(applyID);
        if(us.isEmpty()){
            throw new BeanException("the GroupApplication do not exist");
        }
        return us.get();
    }

    @Override
    public boolean deleteById(long applyID){
        GroupApplication groupApplication = findById(applyID);
        Set<User> examiners = groupApplication.getExaminers();
        for (User examiner : examiners) {
            Set<GroupApplication> groupApplications = examiner.getGroupAppToExam();
            groupApplications.remove(groupApplication);
            examiner.setGroupAppToExam(groupApplications);
        }
        groupApplicationDao.delete(groupApplication);
        return true;
    }
}
