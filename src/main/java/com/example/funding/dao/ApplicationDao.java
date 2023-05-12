package com.example.funding.dao;

import com.example.funding.bean.Application;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ApplicationDao extends CommonDao<Application>{
    @Transactional
    @Modifying
    @Query("update Application a set a.status = ?1 where a.id = ?2")
    int updateStatusById(int status, Long id);

}
