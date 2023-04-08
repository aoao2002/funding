package com.example.funding.dao;

import com.example.funding.bean.User;
import com.example.funding.service.User.UserInfo;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserDao extends CommonDao<User> {

    User findByEmail(String Email);


    User findByName(String name);

    List<User> findAllByName(String name);


    @Override
    Optional<User> findById(Long aLong);

}
