package com.example.funding.dao;

import com.example.funding.bean.Feedback;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface FeedbackDao extends CommonDao<Feedback>{
    @Transactional
    @Modifying
    @Query("update Feedback f set f.read = ?1 where f.id = ?2")
    int updateReadById(int read, Long id);

    Feedback findByApplicationId(Long id);
}
