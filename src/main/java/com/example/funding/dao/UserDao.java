package com.example.funding.dao;

import com.example.funding.bean.Expenditure;
import com.example.funding.bean.Group;
import com.example.funding.bean.User;
import com.example.funding.service.User.UserInfo;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
    Optional<User> findById(@NotNull Long Id);

    User findByEmailAndIdentity(String email, int identity);

    @Query("select u from User u where u.identity = ?1")
    List<User> findByIdentity(int identity);


    List<User> findAllByEmail(String Email);

    @Transactional
    @Modifying
    @Query("update User u set u.expendToExam = ?1 where u.id = ?2")
    int updateExpendToExamById(Expenditure expendToExam, Long id);

    @Transactional
    @Modifying
    @Query("update User u set u.sex = ?1 where u.id = ?2")
    int updateSexById(int sex, Long id);








}
