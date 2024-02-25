package com.aptiv.trainig_tracker.services.serviceImpl;

import com.aptiv.trainig_tracker.domain.Employee;
import com.aptiv.trainig_tracker.domain.Training;
import com.aptiv.trainig_tracker.domain.TrainingTitle;
import com.aptiv.trainig_tracker.domain.TrainingType;
import com.aptiv.trainig_tracker.models.TrainingFromExcel;
import com.aptiv.trainig_tracker.repositories.EmployeeRepo;
import com.aptiv.trainig_tracker.repositories.TrainingRepo;
import com.aptiv.trainig_tracker.repositories.TrainingTitleRepo;
import com.aptiv.trainig_tracker.repositories.TrainingTypeRepo;
import com.aptiv.trainig_tracker.services.TrainingService;
import com.aptiv.trainig_tracker.services.UploadEmployeeData;
import com.aptiv.trainig_tracker.ui.Utils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class TrainingServiceImpl implements TrainingService {
    Utils utils;
    EmployeeRepo employeeRepo;
    TrainingTypeRepo trainingTypeRepo;
    TrainingTitleRepo trainingTitleRepo;
    TrainingRepo trainingRepo;
    @Override
    public void saveTrainingDataToDb(MultipartFile file) throws IllegalAccessException {
        if (UploadEmployeeData.isValidFormat(file)){
            try{
                List<TrainingFromExcel> trainingFromExcels=
                        UploadEmployeeData.getTrainingDataFromExcel(file.getInputStream());
                List<Training> trainings=new ArrayList<>();
                for (TrainingFromExcel tfe: trainingFromExcels){
                    if (trainings.isEmpty()){
                        Training training= new Training();
                        training.setTrainingId(utils.getGeneratedId(22));
                        TrainingType trainingType=trainingTypeRepo.findByTtName(tfe.getTrainingType());
                        if (trainingType==null){
                            TrainingType tt=new TrainingType();
                            tt.setTtName(tfe.getTrainingType());
                            trainingType =trainingTypeRepo.save(tt);
                        }
                        training.setTrainingType(trainingType);
                        TrainingTitle trainingTitle= trainingTitleRepo.findByTTitleName(tfe.getTrainingTitle());
                        if (trainingTitle==null){
                            TrainingTitle tt=new TrainingTitle();
                            tt.setTTitleName(tfe.getTrainingTitle());
                            tt.setTrainingType(trainingType);
                            trainingTitle=trainingTitleRepo.save(tt);
                        }
                        training.setTrainingTitle(trainingTitle);
                        training.setModalite(tfe.getModalite());
                        training.setDureeParHeure(tfe.getDph());
                        training.setDateDebut(tfe.getDdb());
                        training.setDateFin(tfe.getDdf());
                        training.setEva(tfe.isEva());
                        List<Employee> employees=new ArrayList<>();
                        Employee employee=employeeRepo.findByMatricule(tfe.getMatricule());
                        if (employee!=null){
                            employees.add(employee);
                        }
                        training.setEmployees(employees);
                        trainings.add(training);
                    }
                }
            }catch (IOException e){
                throw new RuntimeException(e);
            }
        }
    }
}
