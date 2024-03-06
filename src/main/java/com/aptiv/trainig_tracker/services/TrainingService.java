package com.aptiv.trainig_tracker.services;

import com.aptiv.trainig_tracker.models.SpecTraining;
import com.aptiv.trainig_tracker.models.TrainingDataFormatter;
import com.aptiv.trainig_tracker.models.TrainingFromExcel;
import com.aptiv.trainig_tracker.models.TrainingRest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public interface TrainingService {
    void faMatrixBackup(MultipartFile file) throws IOException;

    void saveTrainingDataToDb(MultipartFile file) throws IllegalAccessException;

    void addTrainingToEmployees(TrainingDataFormatter trainingDataFormatter);

    List<TrainingRest> getAllTrainingBetweenDates(Date stratDate, Date endDate);

    void deleteTrainingFromEmployee(long matricule, String trainingId);

    void deleteSpecTrainingFromEmployee(Date dateDebut, Date dateFin, String title, String type);

    void deletetrainingByID(String trainingId);

    List<TrainingRest> getAllTraining(String trainingId);

    void updateTrainingByTrainingID(TrainingFromExcel trainingFromExcel, String trainingId);

    void updateTrainingByDateAndTitleAndType(Date dateDebut,
                                             Date dateFin,
                                             String title, String type, TrainingFromExcel trainingFromExcel);
}
