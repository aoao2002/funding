package com.example.funding.service.Group;

import com.example.funding.bean.Group;
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
    @Override
    public List<GroupInfo> getAllGroups(){
        List<Group> groups = groupDao.findAll();
        return groups.stream().map(GroupInfo::new).toList();
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
        System.out.println(date);
        group.setCreatedDate(date);
        groupDao.save(group);
        return true;
    }

    public boolean deleteGroup(String groupName){
        //TODO 删除关系表中的关系先，
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
        User user = userDao.findByEmail(manEmail);
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
        User user = userDao.findByEmail(manEmail);
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
