package com.example.funding.dao;

import com.example.funding.bean.Group;
import com.example.funding.bean.User;
import com.example.funding.service.User.UserInfo;
import org.springframework.stereotype.Repository;

import javax.swing.*;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserDao extends CommonDao<User> {

    User findByName(String name);

    List<User> findAllByName(String name);

    List<User> findAllByGroups(Group group);

    @Override
    Optional<User> findById(Long aLong);

    User findByEmailAndIdentity(String email, int identity);

    Set<User> findByIdentity(int identity);

    List<User> findAllByEmail(String Email);




}
