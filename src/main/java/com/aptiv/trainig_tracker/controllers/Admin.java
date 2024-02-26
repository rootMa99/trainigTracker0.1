package com.aptiv.trainig_tracker.controllers;

import com.aptiv.trainig_tracker.models.TrainingDataFormatter;
import com.aptiv.trainig_tracker.services.EmployeeService;
import com.aptiv.trainig_tracker.services.TrainingService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/admin")
@AllArgsConstructor
public class Admin {
    EmployeeService employeeService;
    TrainingService trainingService;

    @PostMapping(path = "/uploadData")
    public void saveDataToDataBase(MultipartFile file) throws IllegalAccessException {
        employeeService.saveEmployeeDataToDb(file);
    }

    @PostMapping(path = "/uploadDataTraining")
    public void saveTrainingDataToDataBase(MultipartFile file) throws IllegalAccessException {
        trainingService.saveTrainingDataToDb(file);
    }

    @PostMapping(path = "/addTrainingToEmployees")
    public void addTrainingToEmployees(TrainingDataFormatter trainingDataFormatter) {
        //trainingService.addTrainingToEmployees(trainingDataFormatter);
        System.out.println(trainingDataFormatter.getTrainingType() + " " + trainingDataFormatter.getTrainingTitle() + " " +
                trainingDataFormatter.getModalite() + " " + trainingDataFormatter.getFormatteur() + " " + trainingDataFormatter.getDdb() + " " + trainingDataFormatter.getDph());
    }
}
