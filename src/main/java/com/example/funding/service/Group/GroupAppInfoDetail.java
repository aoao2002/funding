package com.example.funding.service.Group;

import com.example.funding.bean.GroupApplication;
import com.example.funding.service.User.UserInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class GroupAppInfoDetail {
    private long groupAppId;
    private Date applyTime;
    private String comment;
    private UserInfo applicant;
    private String groupName;
    private long groupId;
    private int status;
    public GroupAppInfoDetail(){}
    public GroupAppInfoDetail(GroupApplication groupApplication){
        this.groupAppId = groupApplication.getId();
        this.applyTime = groupApplication.getApplyTime();
        this.comment = groupApplication.getComment();
        this.groupName = groupApplication.getGroup().getName();
        this.groupId = groupApplication.getGroup().getId();
        this.status = groupApplication.getStatus();
    }
}
