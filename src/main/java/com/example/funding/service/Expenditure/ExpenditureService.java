package com.example.funding.service.Expenditure;

import com.example.funding.bean.Expenditure;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ExpenditureService {
     ExpenditureInfo getOneExpenditureAllInfo(String expenditureNumber);
     List<ExpenditureInfo> getAllExpenditureInfoInOneGroup(String groupName);
     List<ExpenditureInfo> getAllExpenditureInfo();
     boolean updateExpenditureQuota(String expenditureNumber,String quota);
     boolean updateExpenditureEndTime(String expenditureNumber,String endTime);
}
