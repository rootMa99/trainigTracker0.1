package com.aptiv.trainig_tracker.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class TrainingRest {
    private String trainingId;
    private String trainingTitle;
    private String trainingType;
    private String modalite;
    private double dph;
    private Date ddb;
    private Date ddf;
    private String prestataire;
    private String formatteur;
    private boolean eva;
    private List<EmployeeRest> employeeRests;
}
