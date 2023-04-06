package com.example.funding.dao;

import com.example.funding.bean.User;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface UserDao extends CommonDao<User> {

    User findByEmail(String Email);

    User findById(long ID);

    User findByName(String name);
}
