package com.example.funding.bean;

import com.sun.istack.NotNull;
import lombok.*;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@NoArgsConstructor
@Table(name = "all_user")
public class User extends BaseBean{
    //name
    @NotNull
    private String name;

    // email
    @NotNull
    private String email;

    // password
    @NotNull
    private String pw;

    /**
     * 用户身份（0 staff 1 manager 2 president）
     */
    @NotNull
    private int identity;

    @Column(name = "bio", nullable = true)
    private String bio;

    @Column(name = "phone_Number", nullable = true)
    private String phoneNumber;

    /**
     * 账号状态（0 normal 1 abort）
     */
    @Column(name = "status", nullable = true)
    private String status;


    /**
     * 用户性别（0 male，1 female，2 unknown）
     */
    @Column(name = "sex", nullable = true)
    private int sex;

    /**
     * 课题组
     */
    @ManyToMany(cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(name = "user_group", joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "group_id")})
    private Set<Group> groups;

    /**
     * 提交的申请(只有staff才有)
     */
    @OneToMany(mappedBy = "user", cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
//    private Set<Application> applications = new HashSet<>();
    private Set<Application> applications;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
    private Set<GroupApplication> groupApplications;

    /**
     * 提交的反馈(只有manager才有)
     */
    @OneToMany(mappedBy = "user", cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
//    private Set<Feedback> feedbacks = new HashSet<>();
    private Set<Feedback> feedbacks;
}
