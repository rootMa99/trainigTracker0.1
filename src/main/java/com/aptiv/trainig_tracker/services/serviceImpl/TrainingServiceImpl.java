package com.aptiv.trainig_tracker.services.serviceImpl;


import com.aptiv.trainig_tracker.domain.Employee;
import com.aptiv.trainig_tracker.domain.Training;
import com.aptiv.trainig_tracker.domain.TrainingTitle;
import com.aptiv.trainig_tracker.domain.TrainingType;
import com.aptiv.trainig_tracker.models.*;
import com.aptiv.trainig_tracker.repositories.EmployeeRepo;
import com.aptiv.trainig_tracker.repositories.TrainingRepo;
import com.aptiv.trainig_tracker.repositories.TrainingTitleRepo;
import com.aptiv.trainig_tracker.repositories.TrainingTypeRepo;
import com.aptiv.trainig_tracker.services.TrainingService;
import com.aptiv.trainig_tracker.services.UploadEmployeeData;
import com.aptiv.trainig_tracker.ui.Utils;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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
        if (UploadEmployeeData.isValidFormat(file)) {
            try {
                List<TrainingFromExcel> trainingFromExcels =
                        UploadEmployeeData.getTrainingDataFromExcel(file.getInputStream());
                List<TrainingDataFormatter> trainingDataFormatters = formatData(trainingFromExcels);
                List<Training> trainings = new ArrayList<>();
                for (TrainingDataFormatter tfe : trainingDataFormatters) {
                    if (tfe.getTrainingTitle() == null || tfe.getTrainingType() == null || tfe.getModalite() == null) {
                        continue;
                    }
                    Training training = new Training();
                    training.setTrainingId(utils.getGeneratedId(22));
                    TrainingType trainingType = trainingTypeRepo.findByTtName(tfe.getTrainingType());
                    if (trainingType == null) {
                        TrainingType tt = new TrainingType();
                        tt.setTtName(tfe.getTrainingType());
                        trainingType = trainingTypeRepo.save(tt);
                    }
                    training.setTrainingType(trainingType);
                    TrainingTitle trainingTitle = trainingTitleRepo.findByTrainingTitleName(tfe.getTrainingTitle());
                    if (trainingTitle == null) {
                        TrainingTitle tt = new TrainingTitle();
                        tt.setTrainingTitleName(tfe.getTrainingTitle());
                        tt.setTrainingType(trainingType);
                        trainingTitle = trainingTitleRepo.save(tt);
                    }
                    training.setTrainingTitle(trainingTitle);
                    training.setModalite(tfe.getModalite());
                    training.setDureeParHeure(tfe.getDph());
                    training.setDateDebut(tfe.getDdb());
                    training.setDateFin(tfe.getDdf());
                    training.setEva(tfe.isEva());
                    training.setFormatteur(tfe.getFormatteur());
                    training.setPrestataire(tfe.getPrestataire());
                    List<Employee> employees = new ArrayList<>();
                    for (Long l : tfe.getMatricules()) {
                        Employee employee = employeeRepo.findByMatricule(l);
                        if (employee != null) {
                            employees.add(employee);
                        }
                    }
                    training.setEmployees(employees);
                    trainings.add(training);
                }
                trainingRepo.saveAll(trainings);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void addTrainingToEmployees(TrainingDataFormatter trainingDataFormatter) {
        Training training = new Training();
        training.setTrainingId(utils.getGeneratedId(22));
        TrainingType trainingType = trainingTypeRepo.findByTtName(trainingDataFormatter.getTrainingType());
        if (trainingType == null) {
            TrainingType tt = new TrainingType();
            tt.setTtName(trainingDataFormatter.getTrainingType());
            trainingType = trainingTypeRepo.save(tt);
        }
        training.setTrainingType(trainingType);
        TrainingTitle trainingTitle = trainingTitleRepo.findByTrainingTitleName(trainingDataFormatter.getTrainingTitle());
        if (trainingTitle == null) {
            TrainingTitle tt = new TrainingTitle();
            tt.setTrainingTitleName(trainingDataFormatter.getTrainingTitle());
            tt.setTrainingType(trainingType);
            trainingTitle = trainingTitleRepo.save(tt);
        }
        training.setTrainingTitle(trainingTitle);
        training.setModalite(trainingDataFormatter.getModalite());
        training.setDureeParHeure(trainingDataFormatter.getDph());
        training.setDateDebut(trainingDataFormatter.getDdb());
        training.setDateFin(trainingDataFormatter.getDdf());
        training.setEva(trainingDataFormatter.isEva());
        training.setFormatteur(trainingDataFormatter.getFormatteur());
        training.setPrestataire(trainingDataFormatter.getPrestataire());
        List<Employee> employees = new ArrayList<>();
        for (Long l : trainingDataFormatter.getMatricules()) {
            Employee employee = employeeRepo.findByMatricule(l);
            if (employee != null) {
                employees.add(employee);
            }
        }
        training.setEmployees(employees);
        trainingRepo.save(training);
    }

    public List<TrainingDataFormatter> formatData(List<TrainingFromExcel> trainingFromExcels) {
        List<TrainingDataFormatter> trainingDataFormatters = new ArrayList<>();
        for (TrainingFromExcel tfe : trainingFromExcels) {
            if (trainingDataFormatters.isEmpty()) {
                TrainingDataFormatter tdf = new TrainingDataFormatter();
                tdf.setTrainingId(utils.getGeneratedId(22));
                tdf.setModalite(tfe.getModalite());
                tdf.setDph(tfe.getDph());
                tdf.setDdb(tfe.getDdb());
                tdf.setDdf(tfe.getDdf());
                tdf.setPrestataire(tfe.getPrestataire());
                tdf.setFormatteur(tfe.getFormatteur());
                tdf.setEva(tfe.isEva());
                tdf.setTrainingTitle(tfe.getTrainingTitle());
                tdf.setTrainingType(tfe.getTrainingType());
                List<Long> matricules = new ArrayList<>();
                matricules.add(tfe.getMatricule());
                tdf.setMatricules(matricules);
                trainingDataFormatters.add(tdf);
            } else {
                boolean flag = false;
                for (TrainingDataFormatter tf : trainingDataFormatters) {
                    if (tf.getTrainingTitle().equals(tfe.getTrainingTitle()) && tf.getTrainingType().equals(tfe.getTrainingType())
                            && tf.getDdb().equals(tfe.getDdb()) && tf.getDdf().equals(tfe.getDdf())) {
                        flag = true;
                        tf.getMatricules().add(tfe.getMatricule());
                    }
                }
                if (!flag) {
                    TrainingDataFormatter tdf = new TrainingDataFormatter();
                    tdf.setTrainingId(utils.getGeneratedId(22));
                    tdf.setModalite(tfe.getModalite());
                    tdf.setDph(tfe.getDph());
                    tdf.setDdb(tfe.getDdb());
                    tdf.setDdf(tfe.getDdf());
                    tdf.setPrestataire(tfe.getPrestataire());
                    tdf.setFormatteur(tfe.getFormatteur());
                    tdf.setEva(tfe.isEva());
                    tdf.setTrainingTitle(tfe.getTrainingTitle());
                    tdf.setTrainingType(tfe.getTrainingType());
                    List<Long> matricules = new ArrayList<>();
                    matricules.add(tfe.getMatricule());
                    tdf.setMatricules(matricules);
                    trainingDataFormatters.add(tdf);
                }
            }
        }
        return trainingDataFormatters;
    }

    @Override
    public List<TrainingRest> getAllTrainingBetweenDates(Date stratDate, Date endDate) {
        List<Training> trainings = trainingRepo.findAllByDateDebutBetween(stratDate, endDate);
        System.out.println(stratDate + " " + endDate + " " + trainings.size());
        List<TrainingRest> trs = new ArrayList<>();
        for (Training t : trainings) {
            TrainingRest tr = new TrainingRest();
            tr.setTrainingId(t.getTrainingId());
            tr.setModalite(t.getModalite());
            tr.setDph(t.getDureeParHeure());
            tr.setDdb(t.getDateDebut());
            tr.setDdf(t.getDateFin());
            tr.setPrestataire(t.getPrestataire());
            tr.setFormatteur(t.getFormatteur());
            tr.setTrainingType(t.getTrainingType().getTtName());
            tr.setTrainingTitle(t.getTrainingTitle().getTrainingTitleName());
            List<EmployeeRest> employeeRests = getEmployeeRests(t);
            tr.setEmployeeRests(employeeRests);
            trs.add(tr);
        }
        return trs;
    }

    private static List<EmployeeRest> getEmployeeRests(Training t) {
        List<EmployeeRest> employeeRests = new ArrayList<>();
        for (Employee e : t.getEmployees()) {
            EmployeeRest er = new EmployeeRest();
            er.setMatricule(e.getMatricule());
            er.setNom(e.getNom());
            er.setPrenom(e.getPrenom());
            er.setCategory(e.getCategory().getCategoryName());
            er.setFonction(e.getFonctionEntreprise());
            er.setDepartment(e.getDepartment().getDepartmentName());
            er.setPoste(e.getPoste().getPosteName());
            er.setCrew(e.getCrew().getCrewName());
            er.setFamily(e.getFamily().getFamilyName());
            er.setCoordinator(e.getCoordinator().getName());
            er.setShiftLeader(e.getShiftLeader().getName());
            er.setTeamLeader(e.getTeamLeader().getName());
            employeeRests.add(er);
        }
        return employeeRests;
    }

    @Override
    public void deleteTrainingFromEmployee(long matricule, String trainingId) {
        Training training = trainingRepo.findByTrainingId(trainingId);
        training.getEmployees().removeIf(em -> em.getMatricule() == matricule);
        trainingRepo.save(training);
    }

    @Override
    public void deleteSpecTrainingFromEmployee(Date dateDebut,
                                               Date dateFin,
                                               String title, String type) {
        Training training =
                trainingRepo.findByTrainingTypeTtNameAndTrainingTitleTrainingTitleNameAndDateDebutBetween(type,
                        title, dateDebut, dateFin);
        if (training!=null){
            trainingRepo.delete(training);
            System.out.println(training.getTrainingId());
        }else {
            System.out.println("not found");
        }
    }

    @Override
    public void deletetrainingByID(String trainingId) {
        Training training = trainingRepo.findByTrainingId(trainingId);
        trainingRepo.delete(training);
    }

    @Override
    public List<TrainingRest> getAllTraining(String trainingId) {
        List<Training> trainings = trainingRepo.findAllByTrainingId(trainingId);
        List<TrainingRest> trs = new ArrayList<>();
        for (Training t : trainings) {
            TrainingRest tr = new TrainingRest();
            tr.setTrainingId(t.getTrainingId());
            tr.setModalite(t.getModalite());
            tr.setDph(t.getDureeParHeure());
            tr.setDdb(t.getDateDebut());
            tr.setDdf(t.getDateFin());
            tr.setPrestataire(t.getPrestataire());
            tr.setFormatteur(t.getFormatteur());
            tr.setTrainingType(t.getTrainingType().getTtName());
            tr.setTrainingTitle(t.getTrainingTitle().getTrainingTitleName());
            List<EmployeeRest> employeeRests = getEmployeeRests(t);
            tr.setEmployeeRests(employeeRests);
            trs.add(tr);
        }
        return trs;
    }

}
