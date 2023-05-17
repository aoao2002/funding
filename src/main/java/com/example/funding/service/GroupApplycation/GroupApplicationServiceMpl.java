package com.example.funding.service.GroupApplycation;

import com.example.funding.Util.Exception.BeanException;
import com.example.funding.bean.*;
import com.example.funding.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GroupApplicationServiceMpl implements GroupApplicationService{
    @Autowired
    private GroupApplicationDao groupApplicationDao;
    @Override
    public GroupApplication findById(long applyID) {
        Optional<GroupApplication> us = groupApplicationDao.findById(applyID);
        if(us.isEmpty()){
            throw new BeanException("the GroupApplication do not exist");
        }
        return us.get();
    }
}
