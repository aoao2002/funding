package com.example.funding.service.Expenditure;

import com.example.funding.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExpenditureServiceMpl implements ExpenditureService{
    @Autowired
    private ExpenditureDao expenditureDao;
}
