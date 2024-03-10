package com.aptiv.trainig_tracker.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class QualificationRest {
    private String qualificationId;
    private String qualification;
    private List<EmployeeRest> employeeRests;
}
