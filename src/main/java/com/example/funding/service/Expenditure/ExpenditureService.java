package com.example.funding.service.Expenditure;

import cn.dev33.satoken.util.SaResult;
import org.springframework.stereotype.Service;

@Service
public interface ExpenditureService {
     SaResult getOneExpenditureAllInfo(String expenditureNumber);
     SaResult getAllExpenditureInfoInOneGroup(String groupName);
     SaResult getAllExpenditureInfo();
     SaResult updateExpenditureQuota(String expenditureNumber, String quota);
     SaResult updateExpenditureEndTime(String expenditureNumber,String endTime);

     SaResult getOneExpenditureAppAllInfoByMyself(String expenditureNumber);
}
