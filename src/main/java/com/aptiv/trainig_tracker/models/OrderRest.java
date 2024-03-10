package com.aptiv.trainig_tracker.models;

import java.util.Date;
import java.util.List;

public class OrderRest {
    private String qualificationId;
    private String qualification;
    private String shiftLeader;
    private Date qualificationDate;
    private List<EmployeeRest> employeeRests;
}
