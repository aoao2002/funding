package com.example.funding.service.Application;

import com.example.funding.bean.Application;
import com.example.funding.bean.Expenditure;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppInfo {
//    已有基金的情况下的申请
    private long expendId;
    private String expendName;
    private String groupName;
    private double totalAmount;
    private double remainAmount;
    private double availAmount;
    private long useId;
    private String userName;
    private int expendCategory;
    private String appAbstract;
    private double appAmount;
    private String comment;

    private int status;

    public AppInfo(){}
    public AppInfo(Application application){
//        TODO 构造有所区别, 基金第一次注册时前端填前半部分；
//         对app申请时，会通过经费编号然后返回前半部分信息；
//         已有基金申请的时候，前端填的只有后半部分，后端补全然后更新信息；
//         所以还是直接填，不设立对象T_T
        this.expendId = application.getExpenditure().getId();
        this.expendName = application.getExpenditure().getName();
        this.groupName = application.getExpenditure().getGroup().getName();
        this.totalAmount = application.getExpenditure().getTotalAmount();
        this.remainAmount = application.getExpenditure().getRemainingAmount();
        this.availAmount = application.getExpenditure().getRemainingAmount() - application.getExpenditure().getQuota();
        this.useId = application.getUser().getId();
        this.userName = application.getUser().getName();
        this.expendCategory = application.getExpendCategory();
        this.appAbstract = application.getApp_abstract();
        this.appAmount = application.getAmount();
        this.comment = application.getComment();
        this.status = application.getStatus();
    }
    public AppInfo(Expenditure expenditure){
        this.expendId = expenditure.getId();
        this.expendName = expenditure.getName();
        this.groupName = expenditure.getGroup().getName();
        this.totalAmount = expenditure.getTotalAmount();
        this.remainAmount = expenditure.getRemainingAmount();
    }


}
