package com.aptiv.trainig_tracker.controllers;

import com.aptiv.trainig_tracker.models.DateRange;
import com.aptiv.trainig_tracker.models.TrainingDataFormatter;
import com.aptiv.trainig_tracker.models.TrainingRest;
import com.aptiv.trainig_tracker.services.EmployeeService;
import com.aptiv.trainig_tracker.services.TrainingService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

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
    public void addTrainingToEmployees(@RequestBody TrainingDataFormatter trainingDataFormatter) {
        trainingService.addTrainingToEmployees(trainingDataFormatter);
    }

    @GetMapping(path = "/trainingDateBetween")
    public List<TrainingRest> getTrainingsBetweenDates(@RequestParam @DateTimeFormat(pattern = "yyyy-mm-dd") Date dateDebut,
                                                       @RequestParam @DateTimeFormat(pattern = "yyyy-mm-dd") Date dateFin) {

        return trainingService.getAllTrainingBetweenDates(dateDebut, dateFin);
    }

//    @PostMapping(path = "/trainingDateBetween")
//    public List<TrainingRest> getTrainingsBetweenDates(@RequestBody DateRange dateRange) {
//
//        return trainingService.getAllTrainingBetweenDates(dateRange.getStartDate(), dateRange.getEndDate());
//    }
}
