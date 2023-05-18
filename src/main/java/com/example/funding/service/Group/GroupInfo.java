package com.example.funding.service.Group;

import com.example.funding.bean.Group;
import com.example.funding.bean.User;
import com.example.funding.service.Expenditure.ExpenditureInfo;
import com.example.funding.service.User.UserInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Optional;

@Getter
@Setter
public class GroupInfo {
    /**
     * 组的信息包括-名称-成员-基金项目
     */
    private String groupName;
    private List<UserInfo> staffs, managers;
    private List<ExpenditureInfo> expens;
    private List<String> MemberNames;

    public GroupInfo(){}
    public GroupInfo(String groupName){
        this.groupName = groupName;
    }
    public GroupInfo(Optional<Group> group){
        if(group.isPresent()) {
            this.groupName = group.get().getName();
            this.managers = group.get().getUsers().stream().filter(s->s.getIdentity()>0).map(UserInfo::new).toList();
            this.staffs = group.get().getUsers().stream().filter(s->s.getIdentity()==0).map(UserInfo::new).toList();
            this.MemberNames = group.get().getUsers().stream().map(User::getName).toList();
            this.expens = group.get().getExpenditures().stream().map(ExpenditureInfo::new).toList();
        }else{
            System.out.println("this group is not present");
        }
    }
    public GroupInfo(Group group){
        this.groupName = group.getName();
        this.managers = group.getUsers().stream().filter(s->s.getIdentity()>0).map(UserInfo::new).toList();
        this.staffs = group.getUsers().stream().filter(s->s.getIdentity()==0).map(UserInfo::new).toList();
        this.MemberNames = group.getUsers().stream().map(User::getName).toList();
        this.expens = group.getExpenditures().stream().map(ExpenditureInfo::new).toList();
    }

}
