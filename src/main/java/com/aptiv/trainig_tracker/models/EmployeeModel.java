package com.aptiv.trainig_tracker.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class EmployeeModel {
    private Long matricule;
    private String nom;
    private String prenom;
    private String category;
    private String fonction;
    private String department;
    private String poste;
    private String crew;
    private String family;
    private String project;
    private String coordinator;
    private String shiftLeader;
    private String teamLeader;
    private List<TrainingFromExcel> trainingFromExcels;
}
