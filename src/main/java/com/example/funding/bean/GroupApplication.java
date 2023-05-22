package com.example.funding.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.istack.NotNull;
import lombok.*;
import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@NoArgsConstructor
@Table(name = "group_application")

public class GroupApplication extends BaseBean{
    //申请时间
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date applyTime;
    //申请理由
    @Column(name = "comment", nullable = true)
    private String comment;

    /**
     * 申请人， 一个申请只能由一个人提交，一个人可以提交多个申请
     */
    @NotNull
    @ManyToOne(cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;


    @NotNull
    @ManyToOne(cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id")
    private Group group;

    /**
     * 申请的状态（0 未审核 1 审核通过 2 审核未通过， 4 暂存）
     */
    @NotNull
    private int status;

    @ManyToMany(mappedBy = "groupAppToExam", cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
    private Set<User> examiners;
}
