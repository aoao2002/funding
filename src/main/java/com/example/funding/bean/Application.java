package com.example.funding.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.istack.NotNull;
import lombok.*;
import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@NoArgsConstructor
@Table(name = "expenditure_application")
public class Application extends BaseBean{
    //申请时间
//    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date applyTime;
//    baseBean中已经有createTime，去除该量 TODO 这个量可以去除，但是需要扬数据库
    // 申请摘要（用钱来干什么）
    @Column(name = "app_abstract", nullable = true)
    private String app_abstract;
    //申请的附加评论（加急与否）
    @Column(name = "comment", nullable = true)
    private String comment;
    /**
     * 申请人， 一个申请只能由一个人提交，一个人可以提交多个申请
     */
    @NotNull
    @ManyToOne(cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;


    /**
     * 申请的经费，一次申请只能申请一个经费，一个经费可以被多次申请
     */
    @NotNull
    @ManyToOne(cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "expenditure_id")
    private Expenditure expenditure;


    /**
     * 申请的状态（0 未审核 1 审核通过 2 审核未通过 3 撤销）
     */
    @NotNull
    private int status;

    /**
     * 是新建经费项目还是申请经费拨款(0 新建经费项目 1 申请经费拨款)
     * TODO 通过app新建不了，信息不够expend，现在是改了expend来实现
     */
    @NotNull
    private int type;

    /**
     * TODO 加上支出金额以及支出类别
     */
    //支出金额
    @Column(name = "amount", nullable = true)
    private double amount;
    //支出类别，在service/Application/ExpendCategory.java
    @Column(name = "expend_category", nullable = true)
    private int expendCategory;

}
