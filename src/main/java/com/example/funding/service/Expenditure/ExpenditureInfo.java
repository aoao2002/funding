package com.example.funding.service.Expenditure;

import com.example.funding.bean.*;
import com.example.funding.service.Application.AppInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.*;

public class ExpenditureInfo {
    private String expenditureNumber;
    private String expenditureName;
    private String groupName;
    private double totalAmount;
    // remaining_amount
    private double remainingAmount;
    // start_time
    private Date startTime;
    // end_time
    private Date endTime;
    private double quota;

    private List<AppInfo> applications=new ArrayList<>();

    public ExpenditureInfo(Expenditure expenditure){
        this.expenditureNumber = expenditure.getNumber();
        this.expenditureName = expenditure.getName();
        this.groupName = expenditure.getGroup().getName();
        this.totalAmount = expenditure.getTotalAmount();
        this.remainingAmount = expenditure.getRemainingAmount();
        this.startTime = expenditure.getStartTime();
        this.endTime = expenditure.getEndTime();
        this.quota = expenditure.getQuota();
        for (Application application : expenditure.getApplications()) {
            this.applications.add(new AppInfo(application));
        }
    }

    public void setExpenditureNumber(String expenditureNumber) {
        this.expenditureNumber = expenditureNumber;
    }

    public String getExpenditureNumber() {
        return expenditureNumber;
    }

    public void setExpenditureName(String expenditureName) {
        this.expenditureName = expenditureName;
    }

    public String getExpenditureName() {
        return expenditureName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setRemainingAmount(double remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public double getRemainingAmount() {
        return remainingAmount;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }


    public Date getStartTime() {
        return startTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setQuota(double quota) {
        this.quota = quota;
    }

    public double getQuota() {
        return quota;
    }

    public void setApplications(List<AppInfo> applications) {
        this.applications = applications;
    }

    public List<AppInfo> getApplications() {
        return applications;
    }
}
