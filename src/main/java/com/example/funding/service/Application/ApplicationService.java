package com.example.funding.service.Application;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.example.funding.Util.Handler.ReturnHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;

@Service
public interface ApplicationService {

    public SaResult getAppInfoByNumber(String expendNumber, long staffId);
    public SaResult getQuota(String expendNumber);
    public SaResult submitApplication(String expendNumber, String expendCategory1, String expendCategory2, String abstrac ,
                                     String comment, String amount, long userId);
    public SaResult tempSaveApplication(String expendNumber, String expendCategory1, String expendCategory2, String abstrac ,
                                        String comment, String amount, long userId);
    public SaResult getTempSaveApp(Long userId);
    public SaResult withdrawApplication(String appId);
    public SaResult getMyApps(long userId);
    public SaResult getMyAppsOfExpend(String expendNumber, long userId);
    public SaResult getMyAppsToExam(long userId);
    public SaResult passApplication(long userId, String appId, String comment);
    public SaResult rejectApplication(long userId, String appId, String comment);
    public SaResult newExpenditureApplication(String expenditureName, String GroupName, String expenditureNumber,
                                          String expenditureTotalAmount, String BeginTime, String EndTime, long userId) throws ParseException;

    public SaResult submitExpend(String expName, String expNumber, String totalAmound,
                                 String startTime, String endTime, String groupName, long userId) throws ParseException;
    public SaResult tempSaveExpend(String expName, String expNumber, String totalAmound,
                                   String startTime, String endTime, String groupName, long userId) throws ParseException;
    public SaResult getMyExpendsToExam(long userId);
    public SaResult getTempSaveExpend(Long userId);
    public SaResult passExpenditure(long userId, String expId);
    public SaResult getAllMyExpends(long userId);
    public SaResult rejectExpenditure(long userId, String expId);


    SaResult uploadCsvFileToApply(MultipartFile file, long userId);

    SaResult downloadCsvFileApplyFromOneExp(HttpServletResponse response, String expenditureNumber,long userId);
}
