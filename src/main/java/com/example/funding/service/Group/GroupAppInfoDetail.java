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
    private String status;

    public GroupAppInfoDetail(){}
    public GroupAppInfoDetail(GroupApplication groupApplication){
        this.groupAppId = groupApplication.getId();
        this.applyTime = groupApplication.getApplyTime();
        this.comment = groupApplication.getComment();
        this.applicant = new UserInfo(groupApplication.getUser());
        this.groupName = groupApplication.getGroup().getName();
        this.groupId = groupApplication.getGroup().getId();
        String[] statusName = {"Unread", "Pass", "Reject"};
        this.status = statusName[groupApplication.getStatus()];
    }
}
