package com.example.funding.service.Application;

import com.example.funding.bean.Application;
import com.example.funding.bean.Expenditure;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppInfo {
//    已有基金的情况下的申请
    private long appId;
    private long expendId;
    private String expendName;
    private String groupName;
    private double totalAmount;
    private double remainAmount;
    private double availAmount;
    private long userId;
    private String userName;
    private int expendCategory;
    private String appAbstract;
    private double appAmount;
    private String comment;
    private int status;
    private int type;

    public AppInfo(){}

//    默认
    public AppInfo(Application application){
        this.appId = application.getId();
        this.expendId = application.getExpenditure().getId();
        this.expendName = application.getExpenditure().getName();
        this.groupName = application.getExpenditure().getGroup().getName();
        this.totalAmount = application.getExpenditure().getTotalAmount();
        this.remainAmount = application.getExpenditure().getRemainingAmount();
        this.availAmount = this.totalAmount - this.remainAmount;
        this.userId = application.getUser().getId();
        this.userName = application.getUser().getName();
        this.expendCategory = application.getExpendCategory();
        this.appAbstract = application.getApp_abstract();
        this.appAmount = application.getAmount();
        this.comment = application.getComment();
        this.status = application.getStatus();
        this.type = application.getType();
    }
    public AppInfo(Expenditure expenditure){
        this.expendId = expenditure.getId();
        this.expendName = expenditure.getName();
        this.groupName = expenditure.getGroup().getName();
        this.totalAmount = expenditure.getTotalAmount();
        this.remainAmount = expenditure.getRemainingAmount();
    }

}
