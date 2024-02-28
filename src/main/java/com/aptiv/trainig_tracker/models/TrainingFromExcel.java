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
    private String trainingId;
    private Long matricule;
    private String trainingTitle;
    private String trainingType;
    private String modalite;
    private Double dph;
    private Date ddb;
    private Date ddf;
    private String prestataire;
    private String formatteur;
    private boolean eva;
}
