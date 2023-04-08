package com.example.funding.dao;

import com.example.funding.bean.Group;
import com.example.funding.bean.User;
import com.example.funding.service.Group.GroupInfo;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface GroupDao extends CommonDao<Group>{
    Group findByName(String name);

    boolean existsByNameAndUsers_Name(String groupName, String userName);

    boolean existsByNameAndUsers_Id(String name, Long id);

    @Transactional
    @Modifying
    @Query("update Group g set g.users = ?1 where g.name = ?2")
    int updateUsersByName(User users, String name);







}
