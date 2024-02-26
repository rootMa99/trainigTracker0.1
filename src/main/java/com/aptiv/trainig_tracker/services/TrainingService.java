package com.aptiv.trainig_tracker.services;

import com.aptiv.trainig_tracker.models.TrainingDataFormatter;
import com.aptiv.trainig_tracker.models.TrainingRest;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

public interface TrainingService {
    void saveTrainingDataToDb(MultipartFile file) throws IllegalAccessException;
    void addTrainingToEmployees(TrainingDataFormatter trainingDataFormatter);

    List<TrainingRest> getAllTrainingBetweenDates(Date stratDate, Date endDate);
}
