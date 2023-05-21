package com.example.funding.service.Application;

import com.example.funding.bean.Application;
import com.example.funding.bean.Expenditure;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

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
    private String[] expendCategory;
    private String appAbstract;
    private double appAmount;
    private String comment;
    private String status;
    private String type;

    private Date createdDate;
//    0 未审核 1 审核通过 2 审核未通过 3 撤销

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
        ExpendCategory expendCategory1 = getExpendCategory(application.getExpendCategory());
        this.expendCategory = new String[]{expendCategory1.getClass().getSimpleName(), expendCategory1.toString()};
        this.appAbstract = application.getApp_abstract();
        this.appAmount = application.getAmount();
        this.comment = application.getComment();
        if (application.getStatus()>5){
            application.setStatus(5);
        }
        if (application.getType()>2){
            application.setType(2);
        }
        String[] statusName = {"Unread", "Pass", "Reject", "Withdraw", "TempSave", "Error"};
        String[] typeName = {"NewExpend", "NewApply", "Error"};
        this.status = statusName[application.getStatus()];
        this.type = typeName[application.getType()];
        this.createdDate = application.getCreatedDate();
    }
    public AppInfo(Expenditure expenditure){
        this.expendId = expenditure.getId();
        this.expendName = expenditure.getName();
        this.groupName = expenditure.getGroup().getName();
        this.totalAmount = expenditure.getTotalAmount();
        this.remainAmount = expenditure.getRemainingAmount();
    }
    public ExpendCategory getExpendCategory(int categoryCode) {
        switch (categoryCode) {
            case 0: return ExpendCategory.Office.officeSupplies;
            case 1: return ExpendCategory.Office.pen;
            case 2: return ExpendCategory.Office.notebook;
            case 3: return ExpendCategory.Print.print;
            case 4: return ExpendCategory.Print.paper;
            case 5: return ExpendCategory.Maintenance.building;
            case 6: return ExpendCategory.Maintenance.instrument;
            case 7: return ExpendCategory.Maintenance.publicSever;
            case 8: return ExpendCategory.Postage.postage;
            case 9: return ExpendCategory.Postage.telephone;
            case 10: return ExpendCategory.Train.train;
            default: return ExpendCategory.Error.noSuchCategory;
        }
    }

}
