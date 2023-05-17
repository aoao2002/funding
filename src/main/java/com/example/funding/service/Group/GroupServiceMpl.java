package com.example.funding.service.Group;

import cn.dev33.satoken.util.SaResult;
import com.example.funding.Util.Exception.BeanException;
import com.example.funding.Util.Handler.InputChecker;
import com.example.funding.bean.Group;
import com.example.funding.bean.GroupApplication;
import com.example.funding.bean.User;
import com.example.funding.dao.*;
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
//        new application, send it to all managers
        Group group = groupDao.findByName(groupName);
        if(group == null){
            return SaResult.error(String.format("there is no group of %s\n", groupName));
        }
        Optional<User> user = userDao.findById(staffId);
        if (user.isEmpty()){
            return SaResult.error("there is no the staff");
        }
        List<String> groupUsers = group.getUsers().stream().map(s->s.getEmail()+s.getIdentity()).toList();
        if (groupUsers.contains(user.get().getEmail()+user.get().getIdentity())){
            return SaResult.error("you have been in this group");
        }
        Date date = new Date();
        GroupApplication groupApplication = new GroupApplication();
        groupApplication.setUser(userDao.findById(staffId).get());
        groupApplication.setGroup(group);
        groupApplication.setComment(comment);
        groupApplication.setApplyTime(date);
        groupApplication.setStatus(0);
        groupApplicationDao.save(groupApplication);
//        find the corr manager and send it to him
        Set<User> users = group.getUsers();
//        Iterator<User> iterator = users.iterator();
//        TODO 这里可以直接get出来add吗，！！
        user.get().getGroupApplications().add(groupApplication);
        users.stream().filter(s->s.getIdentity()>0).forEach(s->s.getGroupAppToExam().add(groupApplication));
//        userDao.saveAll(users);

        return SaResult.ok();
    }

    public SaResult getMyGroupApplication(long staffId){
        Optional<User> user = userDao.findById(staffId);
        if (user.isEmpty()){
            return SaResult.error("there is no this staff");
        }
        List<GroupAppInfoDetail> groupAppInfoDetails = user
                .get().getGroupApplications().stream().map(GroupAppInfoDetail::new).toList();
        return SaResult.ok().setData(groupAppInfoDetails);

    }
    public SaResult getMyGroupAppToExam(long managerId){
        Optional<User> user = userDao.findById(managerId);
        if (user.isEmpty()){
            return SaResult.error("there is no this staff");
        }
        List<GroupAppInfoDetail> groupAppInfoDetails = user
                .get().getGroupAppToExam().stream().map(GroupAppInfoDetail::new).toList();
        return SaResult.ok().setData(groupAppInfoDetails);
    }

    @Override
    public boolean passApplyGroup(long applyId) {
//        找到application，再找到expen，再找到group，再找到所有manager，所有的application都设置然后删除
        Optional<GroupApplication> groupApplication = groupApplicationDao.findById(applyId);
        if(groupApplication.isEmpty()){
            System.out.printf("there is no such groupApplication of id %d\n", applyId);
            return false;
        }
        groupApplication.get().setStatus(1);
        groupApplicationDao.save(groupApplication.get());
//        该组里所有manager的组申请set中删去这份application
        groupApplication.get().getGroup().getUsers().stream().filter(s->s.getIdentity()>0)
                .forEach(s->s.getGroupApplications().remove(groupApplication.get()));
//        调用join方法将人加入到该组
        this.joinGroup(groupApplication.get().getGroup().getName(), groupApplication.get().getUser().getId());
        return true;
    }

    @Override
    public boolean rejectApplyGroup(long applyId) {
        Optional<GroupApplication> groupApplication = groupApplicationDao.findById(applyId);
        if(groupApplication.isEmpty()){
            System.out.printf("there is no such groupApplication of id %d\n", applyId);
            return false;
        }
        groupApplication.get().setStatus(2);
//        该组里所有manager的组申请set中删去这份application
        groupApplication.get().getGroup().getUsers().stream().filter(s->s.getIdentity()>0)
                .forEach(s->s.getGroupApplications().remove(groupApplication.get()));
        return true;
    }

    public Set<GroupAppInfo> getAllGroupApplicationToBeChecked(long staffId){
        Optional<User> user = userDao.findById(staffId);
        if(user.isEmpty()){
            System.out.printf("something wrong, the staffId of %d is not exist\n", staffId);
            return null;
        }
        return user.get().getGroupApplications().stream().filter(s->s.getStatus() == 0).map(GroupAppInfo::new).collect(Collectors.toSet());
    }

    public Set<GroupInfo> getMyGroups(long staffId){
        Optional<User> user = userDao.findById(staffId);
        if(user.isEmpty()){
            System.out.printf("something wrong, the staffId of %d is not exist\n", staffId);
            return null;
        }
        return user.get().getGroups().stream().map(GroupInfo::new).collect(Collectors.toSet());
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
        Optional<User> user = userDao.findById(staffId);
        if(user.isEmpty()){
            System.out.printf("this user of id %d is not exist\n", staffId);
            return false;
        }
        user.get().getGroups().add(group);
        group.getUsers().add(user.get());
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
        Optional<User> user = userDao.findById(staffId);
        Set<Group> userGroups = user.orElseThrow().getGroups();

        groupUser.remove(user.get());
        userGroups.remove(group);

        group.setUsers(groupUser);
        user.get().setGroups(userGroups);
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
        if(!InputChecker.checkNullAndEmpty(groupName) && !InputChecker.checkNullAndEmpty(groupName)){
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
