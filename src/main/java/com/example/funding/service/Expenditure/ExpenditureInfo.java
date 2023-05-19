package com.example.funding.service.Expenditure;

import com.example.funding.bean.*;
import com.example.funding.service.Application.AppInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
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
    private String status;
//    申请的状态（0 未审核 1 审核通过 2 审核未通过 3 撤销 4 时间停止）
    String[] statusName = {"Unread", "Pass", "Reject", "Withdraw", "Timeout"};

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
        this.status = statusName[expenditure.getStatus()];
        this.applications = expenditure.getApplications().stream().
                sorted(Comparator.comparing(Application::getCreatedDate).reversed()).map(AppInfo::new).toList();
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
