package com.aptiv.trainig_tracker.models;

import lombok.Data;

import java.util.List;

@Data
public class FlexData {
    private Long matricule;
    private List<QualificationModel> qualificationModels;
}
