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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class TrainingServiceImpl implements TrainingService {
    Utils utils;
    EmployeeRepo employeeRepo;
    TrainingTypeRepo trainingTypeRepo;
    TrainingTitleRepo trainingTitleRepo;
    TrainingRepo trainingRepo;

    @Override
    public void saveTrainingDataToDb(MultipartFile file) {
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
                    //System.out.println(tfe.getTrainingType() + " " +
                    //      tfe.getTrainingTitle() + " " + tfe.getDdb() + " " +
                    //    tfe.getDdf());
                    Training trainingf =
                            trainingRepo.findByTrainingTypeTtNameAndTrainingTitleTrainingTitleNameAndDateDebutBetween(tfe.getTrainingType(),
                                    tfe.getTrainingTitle(), tfe.getDdb(),
                                    tfe.getDdf());
                    //System.out.println(trainingf);
                    if (trainingf == null) {
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
                    } else {
                        System.out.println(trainingf + " " + trainingf.getTrainingTitle().getTrainingTitleName());
                        if (trainingf.getEmployees().size() != tfe.getMatricules().size()) {
                            List<Employee> employees = new ArrayList<>();
                            for (Long l : tfe.getMatricules()) {
                                Employee employee = employeeRepo.findByMatricule(l);
                                if (employee != null) {
                                    employees.add(employee);
                                }
                            }
                            trainingf.setEmployees(employees);
                            trainingRepo.save(trainingf);
                        }
                    }

                }
                trainingRepo.saveAll(trainings);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
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
                if (tfe.getDdb() != null && tfe.getDdf() != null && tfe.getDph() != null) {
                    for (TrainingDataFormatter tf : trainingDataFormatters) {
                        if (tf.getTrainingTitle().trim().equals(tfe.getTrainingTitle().trim()) && tf.getTrainingType().trim().equals(tfe.getTrainingType().trim())
                                &&  tf.getDdb().compareTo(tfe.getDdb()) == 0 &&
                                tf.getDdf().compareTo(tfe.getDdf()) == 0) {
                            if (!tfe.getTrainingTitle().equals("Recyclage après shut down")){
                                System.out.println("matching  "+ tfe.getTrainingTitle()+" "+ tfe.getDdb()+" "+ tfe.getTrainingTitle()+" "+ tfe.getDdf());
                            }

                            flag = true;
                            tf.getMatricules().add(tfe.getMatricule());
                        }
                        //System.out.println("test out "+tf.getTrainingTitle()+" "+ tf.getDdb());
                    }
                    //System.out.println(flag+" "+tfe.getTrainingTitle()+" "+ tfe.getDdb()+" "+ tfe.getTrainingTitle
                    // ()+" "+ tfe.getDdf());
                    if (!flag) {
                        System.out.println("not match "+tfe.getTrainingTitle()+" "+ tfe.getDdb()+" "+ tfe.getTrainingTitle()+" "+ tfe.getDdf());
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
        }
        return trainingDataFormatters;
    }

    @Override
    public void addTrainingToEmployees(TrainingDataFormatter trainingDataFormatter) {
        Training trainingf =
                trainingRepo.findByTrainingTypeTtNameAndTrainingTitleTrainingTitleNameAndDateDebutBetween(trainingDataFormatter.getTrainingType(),
                        trainingDataFormatter.getTrainingTitle(), trainingDataFormatter.getDdb(),
                        trainingDataFormatter.getDdf());
        if (trainingf == null) {
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
        } else {
            List<Employee> employees = trainingf.getEmployees();
            for (Long l : trainingDataFormatter.getMatricules()) {
                Employee employee = employeeRepo.findByMatricule(l);
                if (employee != null) {
                    if (!employees.contains(employee)) {
                        employees.add(employee);
                    }
                }
            }
            trainingf.setEmployees(employees);
            trainingRepo.save(trainingf);
        }

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
        if (training != null) {
            trainingRepo.delete(training);
            System.out.println(training.getTrainingId());
        } else throw new RuntimeException("No training Found");
    }

    @Override
    public void deletetrainingByID(String trainingId) {
        Training training = trainingRepo.findByTrainingId(trainingId);
        if (training == null) throw new RuntimeException("training Does Not Exist," +
                " You Try To Delete Formation With ID: " + trainingId);
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

    @Override
    public void updateTrainingByTrainingID(TrainingFromExcel trainingFromExcel, String trainingId) {
        Training training = trainingRepo.findByTrainingId(trainingId);
        if (training == null) throw new RuntimeException("training Does Not Exist, " + trainingId);
        try {
            trainingRepo.save(modifyTraining(trainingFromExcel, training));
        } catch (Exception e) {
            throw new RuntimeException("Something Went Wrong");
        }
    }

    private Training modifyTraining(TrainingFromExcel trainingFromExcel, Training training) {
        if (trainingFromExcel.getTrainingTitle() != null) {
            TrainingTitle trainingTitle = trainingTitleRepo.findByTrainingTitleName(trainingFromExcel.getTrainingTitle());
            training.setTrainingTitle(trainingTitle);
        }
        if (trainingFromExcel.getTrainingType() != null) {
            TrainingType trainingType = trainingTypeRepo.findByTtName(trainingFromExcel.getTrainingType());
            training.setTrainingType(trainingType);
        }
        if (trainingFromExcel.getModalite() != null) {
            training.setModalite(trainingFromExcel.getModalite());
        }
        if (trainingFromExcel.getDph() != null) {
            training.setDureeParHeure(trainingFromExcel.getDph());
        }
        if (trainingFromExcel.getDdb() != null) {
            training.setDateDebut(trainingFromExcel.getDdb());
        }
        if (trainingFromExcel.getDdf() != null) {
            training.setDateFin(trainingFromExcel.getDdf());
        }
        if (trainingFromExcel.getPrestataire() != null) {
            training.setPrestataire(trainingFromExcel.getPrestataire());
        }
        if (trainingFromExcel.getFormatteur() != null) {
            training.setFormatteur(trainingFromExcel.getFormatteur());
        }
        return training;
    }

    @Override
    public void updateTrainingByDateAndTitleAndType(Date dateDebut,
                                                    Date dateFin,
                                                    String title, String type, TrainingFromExcel trainingFromExcel) {
        Training training =
                trainingRepo.findByTrainingTypeTtNameAndTrainingTitleTrainingTitleNameAndDateDebutBetween(type,
                        title, dateDebut, dateFin);
        if (training == null) throw new RuntimeException("training Does Not Exist");
        try {
            trainingRepo.save(modifyTraining(trainingFromExcel, training));
        } catch (Exception e) {
            throw new RuntimeException("Something Went Wrong");
        }
    }
}
