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
     * 用户身份（0 staff 1 manager）
     */
    @NotNull
    private int identity;
    /**
     * 管理的课题组
     */
    @ManyToMany(cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(name = "user_group", joinColumns = {@JoinColumn(name = "user_id")}, inverseJoinColumns = {@JoinColumn(name = "group_id")})
    private Set<Group> groups = new HashSet<>();
    /**
     * 提交的申请(只有staff才有)
     */
    @OneToMany(mappedBy = "user", cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
    private Set<Application> applications = new HashSet<>();
    /**
     * 提交的反馈(只有manager才有)
     */
    @OneToMany(mappedBy = "user", cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
    private Set<Feedback> feedbacks = new HashSet<>();
}
