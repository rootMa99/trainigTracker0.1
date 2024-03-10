package com.aptiv.trainig_tracker.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter @Getter
public class OrderRest {
    private String qualificationId;
    private String qualification;
    private String shiftLeader;
    private Date qualificationDate;
    private Date submitDate;
    private String shift;
    private List<EmployeeRest> employeeRests;
}
