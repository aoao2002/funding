package com.example.funding.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.example.funding.Util.Handler.ReturnHelper;
import com.example.funding.bean.User;
import com.example.funding.service.Group.GroupInfo;
import com.example.funding.service.Group.GroupService;
import com.example.funding.service.User.UserInfo;
import com.example.funding.service.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import cn.dev33.satoken.util.SaResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
//import sun.jvm.hotspot.oops.RawHeapVisitor;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/group/")
public class GroupCtrl {
//    TODO 加入和退出，staff和manager的操作本质上是一样的，得优化一下

    @Autowired
    private GroupService groupService;
    @Autowired
    private UserService userService;

    @RequestMapping(value ="edit/JoinApi", method= RequestMethod.POST)
    @ResponseBody
    public SaResult JoinApi(String groupName){
        // 1. check if name exists
        // 2. if not, insert into database
        System.out.printf("check groupName %s\n", groupName);
        return ReturnHelper.returnBool(groupService.joinGroup(groupName, StpUtil.getLoginIdAsLong()));
    }

    @RequestMapping(value ="view/getAllGroups", method= RequestMethod.GET)
    @ResponseBody
    public SaResult getAllGroups(){
        // 1. get all groups
        return ReturnHelper.returnObj(groupService.getAllGroups());
    }

//    @RequestMapping(value ="view/getGroupsByName", method= RequestMethod.GET)
//    @ResponseBody
//    public SaResult getGroupsByName(String name){
////        return ReturnHelper.returnObj(groupService.getAllGroups());
//        return ReturnHelper.returnBool(false);
//    }

    @RequestMapping(value ="view/getGroupsByUserName", method= RequestMethod.GET)
    @ResponseBody
    public SaResult getGroupsByUserName(String userName){
        return ReturnHelper.returnObj(groupService.getAllGroups());
//        return ReturnHelper.returnBool(false);
    }

    @RequestMapping(value ="view/getMyGroups", method= RequestMethod.GET)
    @ResponseBody
    public SaResult getMyGroups(){
        return ReturnHelper.returnObj(groupService.getMyGroups(StpUtil.getLoginIdAsLong()));
    }



    /**
     * staff's behavior
     */

    @RequestMapping(value = "edit/joinGroup", method = RequestMethod.POST)
    @ResponseBody
    public SaResult joinGroup(String groupName, String comment){
        //   1. check group
        //   2. file json body
        return ReturnHelper.returnBool(groupService.applyGroup(groupName, comment, StpUtil.getLoginIdAsLong()));
    }
    @RequestMapping(value ="edit/quitGroup", method= RequestMethod.POST)
    @ResponseBody
    public SaResult quitGroup(String groupName){
        // 1. check if name exists
        // 2. if exists, delete from database
        return ReturnHelper.returnBool(groupService.quitGroup(groupName, StpUtil.getLoginIdAsLong()));
    }


    /**
     * manager's behavior
     */

    @RequestMapping(value ="edit/modifyGroup", method= RequestMethod.POST)
    @ResponseBody
    public boolean modifyGroup(String name, String updateName){
        // TODO 可以再商量一下，暂时不管
        // 1. check if name exists
        // 2. if exists, update database
        // 3. modify what? add user or add expenditure
        return false;
    }
    @RequestMapping(value ="edit/getMyGroupApplication", method= RequestMethod.POST)
    @ResponseBody
    public SaResult getMyGroupApplication(){
        return ReturnHelper.returnObj(groupService.getAllGroupApplicationToBeChecked(StpUtil.getLoginIdAsLong()));
    }
    @RequestMapping(value = "edit/passApplyGroup", method = RequestMethod.POST)
    @ResponseBody
    public SaResult passApplyGroup(long applyId){
        return ReturnHelper.returnBool(groupService.passApplyGroup(applyId));
    }
    @RequestMapping(value = "edit/rejectApplyGroup", method = RequestMethod.POST)
    @ResponseBody
    public SaResult rejectApplyGroup(long applyId){
        return ReturnHelper.returnBool(groupService.rejectApplyGroup(applyId));
    }


    /**
     * president's behavior
     */

    @RequestMapping(value ="edit/createGroup", method= RequestMethod.POST)
    @ResponseBody
    public SaResult createGroup(String groupName){
        // 1. check if name exists
        // 2. if not, insert into database
        return ReturnHelper.returnBool(groupService.createGroup(groupName));
    }
    @RequestMapping(value ="edit/deleteGroup", method= RequestMethod.POST)
    @ResponseBody
    public SaResult deleteGroup(String groupName){
        // 1. check if name exists
        // 2. if exists, delete from database
        return ReturnHelper.returnBool(groupService.deleteGroup(groupName));
    }
    @RequestMapping(value ="edit/assignManager", method= RequestMethod.POST)
    @ResponseBody
    public SaResult assignManager(String groupName, String manEmail){
        // NOTE 展示成员的时候需要注意有些是管理员
        // 1. check if name exists
        // 2. if exists, update database
        // 3. must be pre
        return ReturnHelper.returnBool(groupService.assignManager(groupName, manEmail));
    }

    @RequestMapping(value ="edit/unassignManager", method= RequestMethod.POST)
    @ResponseBody
    public SaResult unassignManager(String groupName, String manEmail){
        // NOTE 展示成员的时候需要注意有些是管理员
        // 1. check if name exists
        // 2. if exists, update database
        return ReturnHelper.returnBool(groupService.unassignManager(groupName, manEmail));
    }
}
