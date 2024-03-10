package com.aptiv.trainig_tracker.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class OrderDto {
    private String orderId;
    private String qualification;
    private Date orderdate;
    private String shiftLeaderName;
    private String shift;
    private List<Long> matricules;
}
