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
@Table(name = "application")
public class Application extends BaseBean{
    //申请时间
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date applyTime;
    //申请的附加评论
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
     * 申请的状态（0 未审核 1 审核通过 2 审核未通过）
     */
    @NotNull
    private int status;
}
