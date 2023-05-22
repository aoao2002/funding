package com.example.funding.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.istack.NotNull;
import lombok.*;
import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@NoArgsConstructor
@Table(name = "expenditure")
public class Expenditure extends BaseBean{
    //name
    @NotNull
    private String name;
    //sequence number
    @NotNull
    private String number;

    // total_amount
    @NotNull
    private double totalAmount;

    // remaining_amount
    @NotNull
    private double remainingAmount;

    // start_time
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date startTime;

    // end_time
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date endTime;

    /**
     * 经费所属课题组
     */
//    @ManyToMany(mappedBy = "expenditures", cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
//    private Set<Group> groups = new HashSet<>();
    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    /**
     * 经费申请
     */
    @OneToMany(mappedBy = "expenditure", cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
    private Set<Application> applications;

    //TODO: each year usage
    //quota amount
    @Column(name = "quota", nullable = true)
    private double quota;

    /**
     * 申请的状态（0 未审核 1 审核通过 2 审核未通过 3 撤销 4 时间停止）
     */
    @NotNull
    private int status;

//    已经不需要了，但是数据库存着，要改的话需要扬库
    @NotNull
    private int type;

}
