package com.example.funding.service.Application;

import com.example.funding.bean.Group;
import com.example.funding.bean.GroupApplication;
import com.example.funding.bean.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupAppInfo {
    private String applyTime;
    private String comment;
    private long applicantId;
    private long GroupId;
    private int status;
    public GroupAppInfo(){}
    public GroupAppInfo(GroupApplication groupApplication){
        this.applyTime = groupApplication.getApplyTime().toString();
        this.comment = groupApplication.getComment();
        this.applicantId = groupApplication.getUser().getId();
        this.GroupId = groupApplication.getGroup().getId();
        this.status = groupApplication.getStatus();
    }

}
