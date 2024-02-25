package com.aptiv.trainig_tracker.services;

import org.springframework.web.multipart.MultipartFile;

public interface TrainingService {
    void saveTrainingDataToDb(MultipartFile file) throws IllegalAccessException;
}
