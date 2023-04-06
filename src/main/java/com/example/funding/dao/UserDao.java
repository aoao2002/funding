package com.example.funding.dao;

import com.example.funding.bean.User;

import java.util.List;

public interface UserDao extends CommonDao<User> {

    User findByMail(String email);
}
