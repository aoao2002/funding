package com.example.funding.bean;

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
    @ManyToMany(cascade = {CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(name = "user_group", joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "group_id")})
    private Set<Group> groups;

    /**
     * 自己的提交记录(只有staff才有)
     * 需要审批的apps(only for manager)
     * 自己提交的小组申请
     * 需要审批的
     * 自己提交的基金建立
     * 需要审批的
     */
    @OneToMany(mappedBy = "user", cascade = {CascadeType.MERGE}, fetch = FetchType.LAZY)
    private Set<Application> applications;
    @ManyToMany(cascade = {CascadeType.MERGE}, fetch = FetchType.LAZY)
    private Set<Application> appToExam;
    @OneToMany(mappedBy = "user", cascade = {CascadeType.MERGE}, fetch = FetchType.LAZY)
    private Set<GroupApplication> groupApplications;
    @ManyToMany(cascade = {CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(name="all_user_group_app_to_exam", joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "group_app_to_exam_id")})
    private Set<GroupApplication> groupAppToExam;
//    因为基金不属于个人，属于小组
    @ManyToMany(cascade = {CascadeType.MERGE}, fetch = FetchType.LAZY)
    private Set<Expenditure> expenditures;
    @ManyToMany(cascade = {CascadeType.MERGE}, fetch = FetchType.LAZY)
    private Set<Expenditure> expendToExam;

    /**
     * 提交的反馈 【不需要特定的映射】-这样应该可以实现staff的属于收到的feedback，manager属于发出的feedback
     * TODO 这里作为测试尝试一个变量多种用途，上面还是不同用途不同变量
     * 上面确实不适合放在一个，因为这里的操作有可能出现管理员申请
     */
    @ManyToMany(cascade = {CascadeType.MERGE}, fetch = FetchType.LAZY)
    private Set<Feedback> feedbacks;

    /*
    关于邮箱验证：上一次code日期，对应code
     */
    @Column(name = "code_time")
    private Date codeTime;

    @Column(name = "code")
    private String code;


    public static int getPresidentIdentity(){
        return 2;
    }
}
