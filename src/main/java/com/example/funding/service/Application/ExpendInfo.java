package com.example.funding.service.Application;

import com.example.funding.bean.Expenditure;
import lombok.Getter;
import lombok.Setter;

import java.text.SimpleDateFormat;

@Getter
@Setter
public class ExpendInfo {
    long expendId;
    String expendName;
    String expendNumber;
    double totalAmount;
    double remainAmount;
    String startTime;
    String endTime;
    String groupName;
    long groupId;
    double quota; //each year usage allowed
    int status;


    public ExpendInfo(){}
    public ExpendInfo(Expenditure e){
        SimpleDateFormat srtFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        expendId = e.getId();
        expendName = e.getName();
        expendNumber = e.getNumber();
        totalAmount = e.getTotalAmount();
        remainAmount = e.getRemainingAmount();
        startTime = srtFormat.format(e.getStartTime());
        endTime = srtFormat.format(e.getEndTime());
        groupName = e.getGroup().getName();
        groupId = e.getGroup().getId();
        quota = e.getQuota();
        status = e.getStatus();
    }
}
