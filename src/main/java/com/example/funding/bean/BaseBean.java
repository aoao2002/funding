package com.example.funding.bean;

import javax.persistence.*;

@MappedSuperclass
public class BaseBean {

    /**
     * 自增长ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
