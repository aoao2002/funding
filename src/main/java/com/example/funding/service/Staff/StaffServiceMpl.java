package com.example.funding.service.Staff;

import com.example.funding.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StaffServiceMpl implements StaffService {

    @Autowired
    private UserDao userDao;
    @Override
    public void addStaff() {
        System.out.println("addStaff");
    }
}