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
@Table(name = "feedback")
public class Feedback extends BaseBean{
    // 回复意见
    @Column(name = "comment", nullable = true)
    private String comment;
    // 回复时间
//    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date replyTime;
    /**
     * 回复人， 一个回复只能由一个人提交，一个人可以提交多个回复
     */
    @NotNull
    @ManyToOne(cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
    /**
     * 回复的申请，一一对应
     */
    @NotNull
    private long applicationId;
//    表示是否已读 0 为未读,1为已读
    private int read;
}
