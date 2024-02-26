package com.aptiv.trainig_tracker.services;

import com.aptiv.trainig_tracker.models.TrainingDataFormatter;
import org.springframework.web.multipart.MultipartFile;

public interface TrainingService {
    void saveTrainingDataToDb(MultipartFile file) throws IllegalAccessException;
    void addTrainingToEmployees(TrainingDataFormatter trainingDataFormatter);

}
