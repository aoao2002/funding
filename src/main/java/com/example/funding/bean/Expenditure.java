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
@Table(name = "expenditure")
public class Expenditure extends BaseBean{
    //name
    @NotNull
    private String name;
    // total_amount
    @NotNull
    private long totalAmount;
    // remaining_amount
    @NotNull
    private long remainingAmount;
    // start_time
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date startTime;
    // end_time
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date endTime;


}
