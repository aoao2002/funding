package com.example.funding.dao;

import com.example.funding.bean.Expenditure;
import com.example.funding.bean.Group;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ExpenditureDao extends CommonDao<Expenditure>{
    List<Expenditure> findByNumber(String number);

    Expenditure findByNumberAndStatus(String number, int status);


    List<Expenditure> findAllByGroup(Group group);

    @Transactional
    @Modifying
    @Query("update Expenditure e set e.remainingAmount = ?1 where e.number = ?2")
    int updateRemainingAmountByNumber(double remainingAmount, String number);

    @Transactional
    @Modifying
    @Query("update Expenditure e set e.status = ?1 where e.id = ?2")
    int updateStatusById(int status, Long id);

    boolean existsByNumber(String number);

    boolean existsByNumberAndStatus(String number, int status);







}
