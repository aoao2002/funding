package com.example.funding.service.Application;

import cn.dev33.satoken.util.SaResult;
import com.example.funding.Util.Handler.ReturnHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;

@Service
public interface ApplicationService {

    public AppInfo getAppInfoByNumber(String expendNumber, long staffId);
    public SaResult submitApplication(String expendNumber, int expendCategory, String abstrac ,
                                     String comment, double amount, long userId);
    public SaResult withdrawApplication(long appId);
    public SaResult getMyApps(long userId);
    public SaResult getMyAppsToExam(long userId);
    public SaResult passApplication(long userId, long appId);
    public SaResult rejectApplication(long userId, long appId);
    public long newExpenditureApplication(String expenditureName, String GroupName, String expenditureNumber,
                              double expenditureTotalAmount, String BeginTime, String EndTime, long userId) throws ParseException;

    public SaResult submitExpend(String expName, String expNumber, double totalAmound,
                                 String startTime, String endTime, String groupName, long userId) throws ParseException;
    public SaResult getMyExpendsToExam(long userId);
    public SaResult passExpenditure(long userId, long expId);
    public SaResult rejectExpenditure(long userId, long expId);

}
