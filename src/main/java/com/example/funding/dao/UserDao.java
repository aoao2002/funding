package com.example.funding.dao;

import com.example.funding.bean.Expenditure;
import com.example.funding.bean.Group;
import com.example.funding.bean.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserDao extends CommonDao<User> {

    User findByName(String name);

    List<User> findAllByName(String name);

    List<User> findAllByGroups(Group group);

//    @Override
//    Optional<User> findById(@NotNull Long Id);



    User findByEmailAndIdentity(String email, int identity);

    boolean existsByEmailAndIdentity(String email, int identity);


    @Query("select u from User u where u.identity = ?1")
    List<User> findByIdentity(int identity);


    List<User> findAllByEmail(String Email);

    @Transactional
    @Modifying
    @Query("update User u set u.expendToExam = ?1 where u.id = ?2")
    int updateExpendToExamById(Expenditure expendToExam, Long id);

    @Transactional
    @Modifying
    @Query("update User u set u.sex = ?1 where u.id = ?2")
    int updateSexById(int sex, Long id);

    @Transactional
    @Modifying
    @Query("update User u set u.code = ?1 where u.email = ?2 and u.identity = ?3")
    int updateCodeByEmailAndIdentity(String code, String email, int identity);

    @Transactional
    @Modifying
    @Query("update User u set u.status = ?1 where u.id = ?2")
    int updateStatusById(String status, Long id);

    List<User> findByGroups_NameAndIdentity(String name, int identity);

    // 通过id查找用户
    @Transactional
    @Query("select u from User u where u.id = ?1")
    User findByUserId(Long id);

}
