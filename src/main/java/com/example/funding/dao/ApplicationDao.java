package com.example.funding.dao;

import com.example.funding.bean.Application;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import com.example.funding.bean.Expenditure;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ApplicationDao extends CommonDao<Application>{
    List<Application> findAllByExpenditure(Expenditure expenditure);

    @Transactional
    @Modifying
    @Query("update Application a set a.status = ?1 where a.id = ?2")
    int updateStatusById(int status, Long id);

}
