package com.example.funding.dao;

import com.example.funding.bean.GroupApplication;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface GroupApplicationDao extends CommonDao<GroupApplication>{


    @Override
    Optional<GroupApplication> findById(Long aLong);
}