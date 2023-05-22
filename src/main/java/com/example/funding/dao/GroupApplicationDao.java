package com.example.funding.dao;

import com.example.funding.bean.Group;
import com.example.funding.bean.GroupApplication;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Repository
public interface GroupApplicationDao extends CommonDao<GroupApplication>{

    //用id找GroupApplication
    @Transactional
    @Query("select g from GroupApplication g where g.id = ?1")
    GroupApplication findById(long aLong);

    List<GroupApplication> findAllByGroup(Group group);
}
