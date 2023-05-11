package com.example.funding.service.Application;

import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service
public interface ApplicationService {

    public AppInfo getAppInfoByNumber(String expendNumber, long staffId);
    public long submitApplication(String expendNumber, int expendCategory, String abstrac ,
                                     String comment, double amount, long userId);
    public boolean withdrawApplication(long appId);
    public long newExpenditureApplication(String expenditureName, String GroupName, String expenditureNumber,
                              double expenditureTotalAmount, String BeginTime, String EndTime, long userId) throws ParseException;


}
