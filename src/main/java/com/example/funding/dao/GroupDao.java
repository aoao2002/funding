package com.example.funding.dao;

import com.example.funding.bean.Expenditure;
import com.example.funding.bean.Group;
import com.example.funding.bean.User;
import com.example.funding.service.Group.GroupInfo;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Repository
public interface GroupDao extends CommonDao<Group>{
    boolean existsByName(String name);
    Group findByName(String name);

    @Transactional
    @Modifying
    @Query("update Group g set g.createdDate = ?1 where g.id = ?2")
    int updateCreatedDateById(Date createdDate, Long id);


    boolean existsByNameAndUsers_Name(String groupName, String userName);

    boolean existsByNameAndUsers_Id(String name, Long id);

    @Transactional
    @Modifying
    @Query("update Group g set g.users = ?1 where g.name = ?2")
    int updateUsersByName(User users, String name);

    long deleteByName(String name);

    List<Group> findAllByUsers(User user);

}
