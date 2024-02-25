package com.aptiv.trainig_tracker.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class TrainingFromExcel {
    private Long matricule;
    private String trainingTitle;
    private String trainingType;
    private String modalite;
    private double dph;
    private Date ddb;
    private Date ddf;
    private String prestataire;
    private String formatteur;
    private boolean eva;
}
