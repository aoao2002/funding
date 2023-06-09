package com.example.funding.service.Group;

import cn.dev33.satoken.util.SaResult;
import com.example.funding.Util.Exception.BeanException;
import com.example.funding.Util.Handler.InputChecker;
import com.example.funding.bean.Application;
import com.example.funding.bean.Group;
import com.example.funding.bean.GroupApplication;
import com.example.funding.bean.User;
import com.example.funding.dao.*;
import com.example.funding.service.Application.AppInfo;
import com.example.funding.service.Application.GroupAppInfo;
import com.example.funding.service.GroupApplycation.GroupApplicationService;
import com.example.funding.service.User.UserService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class GroupServiceMpl implements GroupService {
    @Autowired
    private GroupDao groupDao;
    @Autowired
    private UserDao userDao;

    @Autowired
    private GroupApplicationService groupApplicationService;

    @Autowired
    private GroupApplicationDao groupApplicationDao;

    @Autowired
    private UserService userService;


    @Override
    public List<GroupInfo> getAllGroups(){
        List<Group> groups = groupDao.findAll();
        return groups.stream().map(GroupInfo::new).toList();
    }

    @Override
    public SaResult applyGroup(String groupName, String comment, long staffId) {
        return saveGroupApplication(groupName, comment, staffId, 0);
    }
    public SaResult tempSaveGroup(String groupName, String comment, long staffId) {
        return saveGroupApplication(groupName, comment, staffId, 4);
    }
    private SaResult saveGroupApplication(String groupName, String comment, long staffId, int saveStatus) {
//        new application, send it to all managers
        Group group = groupDao.findByName(groupName);
        if(group == null){
            return SaResult.error(String.format("there is no group of %s\n", groupName));
        }
        User user = userDao.findByUserId(staffId);
        if (user == null){
            return SaResult.error("there is no the staff");
        }
        Set<GroupApplication> myApp = user.getGroupApplications();
        if (myApp.stream().anyMatch(s->s.getGroup().getName().equals(groupName) && s.getStatus()==0)){
            return SaResult.error("you have submit the application");
        }
        List<String> groupUsers = group.getUsers().stream().map(s->s.getEmail()+s.getIdentity()).toList();
        if (groupUsers.contains(user.getEmail()+user.getIdentity())){
            return SaResult.error("you have been in this group");
        }
        Date date = new Date();
        GroupApplication groupApplication = new GroupApplication();
        groupApplication.setUser(user);
        groupApplication.setGroup(group);
        groupApplication.setComment(comment);
        groupApplication.setApplyTime(date);
        groupApplication.setStatus(saveStatus);
        groupApplication = groupApplicationDao.save(groupApplication);
//        find the corr manager and send it to him
        Set<User> users = group.getUsers();
//        Iterator<User> iterator = users.iterator();
//        TODO 这里可以直接get出来add吗，！！
        user.getGroupApplications().add(groupApplication);
        GroupApplication finalGroupApplication = groupApplication;
        if (saveStatus == 0) {
            users.stream().filter(s -> s.getIdentity() > 0).forEach(s -> s.getGroupAppToExam().add(finalGroupApplication));
            //找到identity大于0的setExaminers
            groupApplication.setExaminers(users.stream().filter(s -> s.getIdentity() > 0).collect(Collectors.toSet()));
        }
//        userDao.saveAll(users);
        return SaResult.ok();
    }
    public SaResult getTempSaveGroup(Long staffId){
        if (staffId== null){
            return SaResult.error("userId is null");
        }
        List<GroupApplication> groupApplications = groupApplicationDao.findByIdAndStatus(staffId, 4);
        if (groupApplications.size() == 0){
            return SaResult.error("no temp save");
        }
        GroupApplication groupApplication = new GroupApplication();
        groupApplication.setComment("no groupApp find");
        return SaResult.ok().setData(new GroupAppInfoDetail(groupApplications.stream()
                .max(Comparator.comparing(GroupApplication::getCreatedDate)).orElse(groupApplication)));

    }

    public SaResult getMyGroupApplication(long staffId){
        User user = userDao.findByUserId(staffId);
        if (user == null){
            return SaResult.error("there is no this staff");
        }
        List<GroupAppInfoDetail> groupAppInfoDetails = user
                .getGroupApplications().stream().map(GroupAppInfoDetail::new).toList();
        return SaResult.ok().setData(groupAppInfoDetails);

    }
    public SaResult getMyGroupAppToExam(long managerId){
        User user = userDao.findByUserId(managerId);
        if (user == null){
            return SaResult.error("there is no this staff");
        }
        List<GroupAppInfoDetail> groupAppInfoDetails = user
                .getGroupAppToExam().stream()
                .filter(s->s.getStatus()==0)
                .map(GroupAppInfoDetail::new).toList();
        return SaResult.ok().setData(groupAppInfoDetails);
    }

    @Override
    public boolean passApplyGroup(long applyId) {
//        找到application，再找到expen，再找到group，再找到所有manager，所有的application都设置然后删除
        GroupApplication groupApplication = groupApplicationDao.findById(applyId);
        if(groupApplication == null){
            System.out.println("there is no this application");
            return false;
        }
        groupApplication.setStatus(1);
        groupApplicationDao.save(groupApplication);
//        该组里所有manager的组申请set中删去这份application
        groupApplication.getGroup().getUsers().stream().filter(s->s.getIdentity()>0)
                .forEach(s->s.getGroupApplications().remove(groupApplication));
//        调用join方法将人加入到该组
        this.joinGroup(groupApplication.getGroup().getName(), groupApplication.getUser().getId());
        return true;
    }

    @Override
    public boolean rejectApplyGroup(long applyId) {
        GroupApplication groupApplication = groupApplicationDao.findById(applyId);
        if(groupApplication == null){
            System.out.println("there is no this application");
            return false;
        }
        groupApplication.setStatus(2);
//        该组里所有manager的组申请set中删去这份application
        groupApplication.getGroup().getUsers().stream().filter(s->s.getIdentity()>0)
                .forEach(s->s.getGroupApplications().remove(groupApplication));
        return true;
    }

    public Set<GroupAppInfo> getAllGroupApplicationToBeChecked(long staffId){
        User user = userDao.findByUserId(staffId);
        if(user == null){
            System.out.println("there is no this staff");
            return null;
        }
        return user.getGroupApplications().stream().filter(s->s.getStatus() == 0).map(GroupAppInfo::new).collect(Collectors.toSet());
    }

    public Set<GroupInfo> getMyGroups(long staffId){
        User user = userDao.findByUserId(staffId);
        if(user == null){
            System.out.println("there is no this staff");
            return null;
        }
        return user.getGroups().stream().map(GroupInfo::new).collect(Collectors.toSet());
    }

    @Override
    public boolean joinGroup(String groupName, long staffId){
        if(groupDao.existsByNameAndUsers_Id(groupName, staffId)){
            return false;
        }
        Group group = groupDao.findByName(groupName);
        if(group == null){
            System.out.printf("this group of name %s is null\n", groupName);
            return false;
        }
//        Set<User> groupUsers = group.getUsers();
        User user = userDao.findByUserId(staffId);
        if(user == null){
            System.out.printf("this user of id %d is not exist\n", staffId);
            return false;
        }
        user.getGroups().add(group);
        group.getUsers().add(user);
//        Set<Group> userGroups = user.get().getGroups();
//        userGroups.add(group);
//        user.get().setGroups(userGroups);
//        groupUsers.add(user.get());
//        group.setUsers(groupUsers);
        return true;
    }

    public boolean quitGroup(String groupName, long staffId){
        if(!groupDao.existsByNameAndUsers_Id(groupName, staffId)){
            return false;
        }
        Group group = groupDao.findByName(groupName);
        if(group == null){
            System.out.printf("this group of name %s is null\n", groupName);
            return false;
        }
//      find the group and the user, find groupUsers and userGroups relatively and add
//      , and it might be duplicate to judge null
        Set<User> groupUser = group.getUsers();
        User user = userDao.findByUserId(staffId);
        if (user == null){
            throw new RuntimeException("this user is not exist");
        }
        Set<Group> userGroups = user.getGroups();

        groupUser.remove(user);
        userGroups.remove(group);

        group.setUsers(groupUser);
        user.setGroups(userGroups);
        return true;
    }

    /*
    using groupDao.existsByName(groupName), the IDEA would skip following code if it is null by finding groupName, raise an error
     */
    public boolean createGroup(String groupName){
        if(groupDao.existsByName(groupName)){
            System.out.printf("this group of %s is exist\n", groupName);
            return false;
        }
        System.out.println("this group does not exist");
        Group group = new Group();
        group.setName(groupName);
        group.setUsers(new HashSet<>());
        group.setExpenditures(new HashSet<>());
        Date date = new Date();
        group.setCreatedDate(date);
        List<User> admins = userDao.findByIdentity(2);
        admins.forEach(s->group.getUsers().add(s));
        Group group1 = groupDao.save(group);
        admins.forEach(s-> {
            s.getGroups().add(group1);
            System.out.printf("in createGroup, %s\n", s.getName());
        });
        userDao.saveAll(admins);
//        groupDao.updateCreatedDateById(new Date(), group1.getId());
        return true;
    }

    public boolean deleteGroup(String groupName){
        Group group = groupDao.findByName(groupName);
        Set<User> groupUsers = group.getUsers();
//        groupUsers.stream().forEach(s->s.setGroups(s.getGroups().remove(group))); //how to handle this to avoid for loop
        for (User user : groupUsers) {
            Set<Group> userGroups = user.getGroups();
            userGroups.remove(group);
            user.setGroups(userGroups);
        }
        List<GroupApplication> groupApplications = groupApplicationDao.findAllByGroup(group);
        for (GroupApplication groupApplication : groupApplications) {
            groupApplicationService.deleteById(groupApplication.getId());
        }
        group.setUsers(new HashSet<>());
        return groupDao.deleteByName(groupName)>0;
    }

    public boolean assignManager(String groupName, String manEmail){
        if(!groupDao.existsByName(groupName)){
            System.out.printf("this group of %s is not exist\n", groupName);
            return false;
        }
        Group group = groupDao.findByName(groupName);
        if(group == null){
            System.out.printf("this group of name %s is null\n", groupName);
            return false;
        }
        User user = userDao.findByEmailAndIdentity(manEmail, 1);
        if(user == null){
            System.out.printf("this user of email %s is null\n", manEmail);
            return false;
        }
        Set<User> groupUsers = group.getUsers();
        Set<Group> userGroups = user.getGroups();
        groupUsers.add(user);
        userGroups.add(group);
        group.setUsers(groupUsers);
        user.setGroups(userGroups);
        return true;
    }

    public boolean unassignManager(String groupName, String manEmail){
        if(!groupDao.existsByName(groupName)){
            System.out.printf("this group of %s is not exist\n", groupName);
            return false;
        }
        Group group = groupDao.findByName(groupName);
        if(group == null){
            System.out.printf("this group of name %s is null\n", groupName);
            return false;
        }
        User user = userDao.findByEmailAndIdentity(manEmail, 1);
        if(user == null){
            System.out.printf("this user of email %s is null\n", manEmail);
            return false;
        }
        Set<User> groupUsers = group.getUsers();
        Set<Group> userGroups = user.getGroups();
        groupUsers.remove(user);
        userGroups.remove(group);
        group.setUsers(groupUsers);
        user.setGroups(userGroups);
        return true;
    }

    @Override
    public boolean checkGroupEditPower(String applyID, String groupName, String UserID) {
        if(!InputChecker.checkNullAndEmpty(groupName)){
            throw new BeanException("you need offer either applyID or groupName to edit group");
        }
        if(!InputChecker.checkNullAndEmpty(Lists.newArrayList(UserID))){
            throw new BeanException("do not offer user for edit group");
        }
        User user = userService.findById(Long.parseLong(UserID));

        if(user==null) throw new BeanException("UserID wrong for edit group");

        Group group = groupDao.findByName(groupName);
        if(group==null) {
            //可能是通过applyID请求的，尝试找到对应group

            GroupApplication groupApplication =  groupApplicationService.findById(Long.parseLong(applyID));
            if (groupApplication == null) throw new BeanException("groupApplication do not exit");

            group = groupApplication.getGroup();
            if(group==null) throw new BeanException("group do not exit");
        }

        return user.getIdentity() != 0 && group.getUsers().contains(user);
    }

    /**
     * test only
     */
    public boolean assignStaff(String groupName, String manEmail){
        if(!groupDao.existsByName(groupName)){
            System.out.printf("this group of %s is not exist\n", groupName);
            return false;
        }
        Group group = groupDao.findByName(groupName);
        if(group == null){
            System.out.printf("this group of name %s is null\n", groupName);
            return false;
        }
        User user = userDao.findByEmailAndIdentity(manEmail, 0);
        if(user == null){
            System.out.printf("this user of email %s is null\n", manEmail);
            return false;
        }
        Set<User> groupUsers = group.getUsers();
        Set<Group> userGroups = user.getGroups();
        groupUsers.add(user);
        userGroups.add(group);
        group.setUsers(groupUsers);
        user.setGroups(userGroups);
        return true;
    }
    public boolean unassignStaff(String groupName, String manEmail){
        if(!groupDao.existsByName(groupName)){
            System.out.printf("this group of %s is not exist\n", groupName);
            return false;
        }
        Group group = groupDao.findByName(groupName);
        if(group == null){
            System.out.printf("this group of name %s is null\n", groupName);
            return false;
        }
        User user = userDao.findByEmailAndIdentity(manEmail, 0);
        if(user == null){
            System.out.printf("this user of email %s is null\n", manEmail);
            return false;
        }
        Set<User> groupUsers = group.getUsers();
        Set<Group> userGroups = user.getGroups();
        groupUsers.remove(user);
        userGroups.remove(group);
        group.setUsers(groupUsers);
        user.setGroups(userGroups);
        return true;
    }
}
