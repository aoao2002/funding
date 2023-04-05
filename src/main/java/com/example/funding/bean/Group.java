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
@Table(name = "all_group")
public class Group extends BaseBean{
    //name
    @NotNull
    private String name;
    /**
     * 课题组成员
     */
    @ManyToMany(mappedBy = "groups", cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
    private Set<User> users = new HashSet<>();
    /**
     * 课题组下的经费
     */
    @ManyToMany(cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(name = "group_expenditure", joinColumns = {@JoinColumn(name = "group_id")}, inverseJoinColumns = {@JoinColumn(name = "expenditure_id")})
    private Set<Expenditure> expenditures = new HashSet<>();

}
