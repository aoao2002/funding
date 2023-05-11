package com.example.funding.service.Group;

import com.example.funding.bean.Expenditure;
import com.example.funding.bean.Group;
import com.example.funding.bean.User;
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
    private List<String> memberNames;
    private List<String> expenNames;

    public GroupInfo(){}
    public GroupInfo(String groupName){
        this.groupName = groupName;
    }
    public GroupInfo(Optional<Group> group){
        if(group.isPresent()) {
            this.groupName = group.get().getName();
            this.memberNames = group.get().getUsers().stream().map(User::getName).toList();
            this.expenNames = group.get().getExpenditures().stream().map(Expenditure::getName).toList();
        }else{
            System.out.println("this group is not present");
        }
    }
    public GroupInfo(Group group){
        this.groupName = group.getName();
        this.memberNames = group.getUsers().stream().map(User::getName).toList();
        this.expenNames = group.getExpenditures().stream().map(Expenditure::getName).toList();
    }

    public String getGroupName() {
        return groupName;
    }

    public List<String> getMemberNames() {
        return memberNames;
    }

    public List<String> getExpenNames() {
        return expenNames;
    }
}
