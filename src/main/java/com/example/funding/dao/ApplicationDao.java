package com.example.funding.dao;

import com.example.funding.bean.Application;
import com.example.funding.bean.Expenditure;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationDao extends CommonDao<Application>{
     List<Application> findAllByExpenditure(Expenditure expenditure);
}
