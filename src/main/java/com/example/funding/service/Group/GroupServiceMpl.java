package com.example.funding.service.Group;

import com.example.funding.bean.Application;
import com.example.funding.bean.Group;
import com.example.funding.bean.GroupApplication;
import com.example.funding.bean.User;
import com.example.funding.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.util.*;

@Service
public class GroupServiceMpl implements GroupService {
    @Autowired
    private GroupDao groupDao;
    @Autowired
    private UserDao userDao;

    @Autowired
    private GroupApplicationDao groupApplicationDao;
    @Override
    public List<GroupInfo> getAllGroups(){
        List<Group> groups = groupDao.findAll();
        return groups.stream().map(GroupInfo::new).toList();
    }

    @Override
    public boolean applyGroup(String groupName, String comment, long staffId) {
//        new application, send it to all managers
        Group group = groupDao.findByName(groupName);
        if(group == null){
            System.out.printf("there is no group of %s\n", groupName);
            return false;
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
        users.stream().filter(s->s.getIdentity()>0).forEach(s->s.getGroupApplications().add(groupApplication));
        return true;
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
        groupApplication.get().getGroup().getUsers().stream().filter(s->s.getIdentity()>0).forEach(s->s.getGroupApplications().remove(groupApplication));

        return false;
    }

    @Override
    public boolean rejectApplyGroup(long applyId) {
        return false;
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
        Set<User> groupUsers = group.getUsers();
        Optional<User> user = userDao.findById(staffId);
        if(user.isEmpty()){
            System.out.printf("this user of id %d is not exist\n", staffId);
            return false;
        }
        Set<Group> userGroups = user.get().getGroups();
        userGroups.add(group);
        user.get().setGroups(userGroups);
        groupUsers.add(user.get());
        group.setUsers(groupUsers);
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
        Date date = new Date();
        group.setCreatedDate(date);
        group.setUsers(new HashSet<>(userDao.findByIdentity(2)));
        groupDao.save(group);
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
    public boolean checkGroupName(String groupName){

        return false;
    }
}
