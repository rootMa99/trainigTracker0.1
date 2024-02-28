package com.aptiv.trainig_tracker.controllers;

import com.aptiv.trainig_tracker.models.*;
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
    public List<TrainingRest> getTrainingsBetweenDates(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateDebut,
                                                       @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateFin) {
        System.out.println(dateDebut+" "+dateFin);
        return trainingService.getAllTrainingBetweenDates(dateDebut, dateFin);
    }

    @PostMapping(path = "/trainingDateBetween")
    public List<TrainingRest> getTrainingsBetweenDates(@RequestBody DateRange dateRange) {

        return trainingService.getAllTrainingBetweenDates(dateRange.getStartDate(), dateRange.getEndDate());
    }
    @DeleteMapping(path = "/deleteTrainingFe")
    public void deleteTrainingFromEmployee(@RequestParam long matricule, @RequestParam String trainingID){
        trainingService.deleteTrainingFromEmployee(matricule, trainingID);
    }
    @DeleteMapping(path = "/deleteTraining")
    public void deleteTrainingFromEmployee(@RequestParam String trainingID){
        trainingService.deletetrainingByID(trainingID);
    }
    @DeleteMapping(path = "/deleteTrainingByDateAndTitleAndType")
    public void deleteTrainingFromEmployee(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateDebut,
                                           @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateFin,
                                           @RequestParam String title, @RequestParam String type){
        trainingService.deleteSpecTrainingFromEmployee(dateDebut, dateFin, title, type);
    }
    @GetMapping(path = "/allTrainingsById")
    public List<TrainingRest> getTrainings(@RequestParam String trainingID) {
        return trainingService.getAllTraining(trainingID);
    }

    @PutMapping(path = "/updateTrainingById")
    public OperationStatusResult upDateTrainingById(@RequestParam String trainingId,@RequestBody TrainingFromExcel trainingFromExcel){
        trainingService.updateTrainingByTrainingID(trainingFromExcel, trainingId);
        return new OperationStatusResult("UPDATE", "SUCCESS");
    }
}
