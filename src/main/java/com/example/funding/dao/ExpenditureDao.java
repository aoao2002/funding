package com.example.funding.dao;

import com.example.funding.bean.Expenditure;
import com.example.funding.bean.Group;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenditureDao extends CommonDao<Expenditure>{
    Expenditure findByNumber(String number);

    List<Expenditure> findAllByGroup(Group group);
}
