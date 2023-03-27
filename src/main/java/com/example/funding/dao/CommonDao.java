package com.example.funding.dao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.funding.bean.BaseBean;

@Repository
public interface CommonDao<T extends BaseBean> extends JpaRepository<T, Long> {

}
