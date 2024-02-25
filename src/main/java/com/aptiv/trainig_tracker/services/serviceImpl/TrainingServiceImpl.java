package com.aptiv.trainig_tracker.services.serviceImpl;

import com.aptiv.trainig_tracker.domain.Training;
import com.aptiv.trainig_tracker.models.TrainingFromExcel;
import com.aptiv.trainig_tracker.repositories.EmployeeRepo;
import com.aptiv.trainig_tracker.repositories.TrainingRepo;
import com.aptiv.trainig_tracker.repositories.TrainingTitleRepo;
import com.aptiv.trainig_tracker.repositories.TrainingTypeRepo;
import com.aptiv.trainig_tracker.services.TrainingService;
import com.aptiv.trainig_tracker.services.UploadEmployeeData;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class TrainingServiceImpl implements TrainingService {

    @Override
    public void saveTrainingDataToDb(MultipartFile file) throws IllegalAccessException {
        TrainingRepo trainingRepo;
        TrainingTitleRepo trainingTitleRepo;
        TrainingTypeRepo trainingTypeRepo;
        EmployeeRepo employeeRepo;

        if (UploadEmployeeData.isValidFormat(file)){
            try{
                List<TrainingFromExcel> trainingFromExcels=
                        UploadEmployeeData.getTrainingDataFromExcel(file.getInputStream());
                List<Training> trainings=new ArrayList<>();
                for (TrainingFromExcel tfe: trainingFromExcels){

                }
            }catch (IOException e){
                throw new RuntimeException(e);
            }
        }
    }
}
