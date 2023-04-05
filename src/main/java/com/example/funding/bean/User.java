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
    // password
    @NotNull
    private String pw;
    /**
     * 用户身份（0 staff 1 manager）
     */
    @Column(name = "identity", nullable = true)
    private int identity;
    /**
     * 管理的课题组
     */
    @ManyToMany(cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(name = "user_group", joinColumns = {@JoinColumn(name = "user_id")}, inverseJoinColumns = {@JoinColumn(name = "group_id")})
    private Set<Group> groups = new HashSet<>();

}
