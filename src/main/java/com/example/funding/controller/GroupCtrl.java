package com.example.funding.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.example.funding.Util.Handler.ReturnHelper;
import com.example.funding.service.Group.GroupService;
import com.example.funding.service.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
//import sun.jvm.hotspot.oops.RawHeapVisitor;


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
        System.out.printf("check groupName %s\n", groupName);
        return ReturnHelper.returnBool(groupService.joinGroup(groupName, StpUtil.getLoginIdAsLong()));
    }

    @RequestMapping(value ="getAllGroups", method= RequestMethod.GET)
    @ResponseBody
    public SaResult getAllGroups(){
        return ReturnHelper.returnObj(groupService.getAllGroups());
    }


    @RequestMapping(value ="getGroupsByUserName", method= RequestMethod.GET)
    @ResponseBody
    public SaResult getGroupsByUserName(String userName){
        return ReturnHelper.returnObj(groupService.getAllGroups());
    }

    @RequestMapping(value ="getMyGroups", method= RequestMethod.GET)
    @ResponseBody
    public SaResult getMyGroups(){
        return ReturnHelper.returnObj(groupService.getMyGroups(StpUtil.getLoginIdAsLong()));
    }



    /**
     * staff's behavior
     * no need to check authority
     */

    @RequestMapping(value = "joinGroup", method = RequestMethod.POST)
    @ResponseBody
    public SaResult joinGroup(String groupName, String comment){
       // TODO：this api just for test
        return groupService.applyGroup(groupName, comment, StpUtil.getLoginIdAsLong());
    }

    @RequestMapping(value = "tempSaveGroupApp", method = RequestMethod.POST)
    @ResponseBody
    public SaResult tempSaveGroupApp(String groupName, String comment){
        // TODO：this api just for test
        return groupService.tempSaveGroup(groupName, comment, StpUtil.getLoginIdAsLong());
    }

    @RequestMapping(value ="quitGroup", method= RequestMethod.POST)
    @ResponseBody
    public SaResult quitGroup(String groupName){
        return ReturnHelper.returnBool(groupService.quitGroup(groupName, StpUtil.getLoginIdAsLong()));
    }

//  获得自己申请过的记录
    @RequestMapping(value ="getMyGroupApplication", method= RequestMethod.GET)
    @ResponseBody
    public SaResult getMyGroupApplication(){
        return groupService.getMyGroupApplication(StpUtil.getLoginIdAsLong());
    }




    /**
     * manager's behavior
     *
     * the input of each api need include the group name
     */

    @RequestMapping(value ="getMyGroupAppToExam", method= RequestMethod.GET)
    @ResponseBody
    public SaResult getMyGroupAppToExam(){
        return groupService.getMyGroupAppToExam(StpUtil.getLoginIdAsLong());
    }



    @RequestMapping(value ="edit/modifyGroup", method= RequestMethod.POST)
    @ResponseBody
    public boolean modifyGroup(String groupName, String updateName){
        // TODO 可以再商量一下，暂时不管
        // 1. check if name exists
        // 2. if exists, update database
        // 3. modify what? add user or add expenditure
        return false;
    }
//    @RequestMapping(value ="getMyGroupApplication", method= RequestMethod.POST)
//    @ResponseBody
//    public SaResult getMyGroupApplication(){
//        return ReturnHelper.returnObj(groupService.getAllGroupApplicationToBeChecked(StpUtil.getLoginIdAsLong()));
//    }

    @RequestMapping(value = "edit/passApplyGroup", method = RequestMethod.POST)
    @ResponseBody
    public SaResult passApplyGroup(long applyId){
        // to apply for join the group
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

    @RequestMapping(value ="president/createGroup", method= RequestMethod.POST)
    @ResponseBody
    public SaResult createGroup(String groupName){

        return ReturnHelper.returnBool(groupService.createGroup(groupName));
    }
    @RequestMapping(value ="president/deleteGroup", method= RequestMethod.POST)
    @ResponseBody
    public SaResult deleteGroup(String groupName){

        return ReturnHelper.returnBool(groupService.deleteGroup(groupName));
    }
    @RequestMapping(value ="president/assignManager", method= RequestMethod.POST)
    @ResponseBody
    public SaResult assignManager(String groupName, String manEmail){

        return ReturnHelper.returnBool(groupService.assignManager(groupName, manEmail));
    }

    @RequestMapping(value ="president/unassignManager", method= RequestMethod.POST)
    @ResponseBody
    public SaResult unassignManager(String groupName, String manEmail){

        return ReturnHelper.returnBool(groupService.unassignManager(groupName, manEmail));
    }
}
