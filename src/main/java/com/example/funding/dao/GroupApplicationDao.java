package com.example.funding.dao;

import com.example.funding.bean.Group;
import com.example.funding.bean.GroupApplication;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface GroupApplicationDao extends CommonDao<GroupApplication>{


    GroupApplication findById(long aLong);

    List<GroupApplication> findAllByGroup(Group group);
}
