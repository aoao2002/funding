package com.example.funding.service.Group;

import com.example.funding.bean.Group;
import com.example.funding.bean.User;
import com.example.funding.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    public boolean checkGroupName(String groupName, long staffId){
        return false;
    }
}
