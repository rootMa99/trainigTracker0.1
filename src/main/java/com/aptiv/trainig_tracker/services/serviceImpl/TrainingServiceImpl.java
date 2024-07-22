package com.aptiv.trainig_tracker.services.serviceImpl;


import com.aptiv.trainig_tracker.domain.*;
import com.aptiv.trainig_tracker.models.*;
import com.aptiv.trainig_tracker.repositories.*;
import com.aptiv.trainig_tracker.services.TrainingService;
import com.aptiv.trainig_tracker.services.UploadEmployeeData;
import com.aptiv.trainig_tracker.ui.Utils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TrainingServiceImpl implements TrainingService {
    Utils utils;
    EmployeeRepo employeeRepo;
    TrainingTypeRepo trainingTypeRepo;
    TrainingTitleRepo trainingTitleRepo;
    TrainingRepo trainingRepo;
    QualificationEmployeeRepo qualificationEmployeeRepo;
    QualificationRepo qualificationRepo;

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


    @Transactional
    @Override
    public void qualificationData(MultipartFile file) throws IOException {
        if (!UploadEmployeeData.isValidFormat(file)) {
            throw new IllegalArgumentException("Invalid file format");
        }

        List<FlexData> lfd = UploadEmployeeData.getQualifications(file.getInputStream());
        if (lfd.isEmpty()) {
            return;
        }

        Map<Long, Employee> employeeCache = new HashMap<>();
        Map<String, Qualification> qualificationCache = new HashMap<>();

        // Bulk fetch employees
        List<Long> matricules = lfd.stream().map(FlexData::getMatricule).distinct().toList();
        List<Employee> employees = employeeRepo.findByMatriculeIn(matricules);
        for (Employee em : employees) {
            employeeCache.put(em.getMatricule(), em);
        }

        // Bulk fetch qualifications
        List<String> qualificationNames = lfd.stream()
                .flatMap(fd -> fd.getQualificationModels().stream())
                .map(QualificationModel::getQualificationName)
                .distinct()
                .toList();
        List<Qualification> qualifications = qualificationRepo.findByNameIn(qualificationNames);
        for (Qualification q : qualifications) {
            qualificationCache.put(q.getName(), q);
        }

        for (FlexData fd : lfd) {
            Employee em = employeeCache.get(fd.getMatricule());
            if (em == null || fd.getQualificationModels().isEmpty()) {
                System.err.println("Employee not found for matricule: " + fd.getMatricule());
                continue;
            }

            for (QualificationModel qm : fd.getQualificationModels()) {
                Qualification q = qualificationCache.get(qm.getQualificationName());
                if (q == null) {
                    q = new Qualification();
                    q.setName(qm.getQualificationName());
                    q = qualificationRepo.save(q);
                    qualificationCache.put(q.getName(), q);
                }

                QualificationEmployeeId id = new QualificationEmployeeId(em.getMatricule(), q.getId());

                Optional<QualificationEmployee> existingQE = qualificationEmployeeRepo.findById(id);
                if (existingQE.isPresent()) {
                    System.err.println("QualificationEmployee already exists: " + id);
                    continue;
                }

                QualificationEmployee qe = new QualificationEmployee();
                qe.setId(id);
                qe.setEmployee(em);
                qe.setQualification(q);
                switch (qm.getStatus()) {
                    case "R":
                        qe.setStatus(Status.R);
                        break;
                    case "C":
                        qe.setStatus(Status.C);
                        break;
                    case "F":
                        qe.setStatus(Status.F);
                        break;
                    case "X":
                        qe.setStatus(Status.X);
                        System.err.println("X status: " + qm.getStatus() + " " + fd.getMatricule());
                        break;
                    default:
                        System.err.println("Unknown status: " + qm.getStatus());
                        continue;
                }

                qualificationEmployeeRepo.save(qe);
            }
        }
    }


    @Override
    public void faMatrixBackup(MultipartFile file) throws IOException {
        if (UploadEmployeeData.isValidFormat(file)) {
            List<FaMatrixGlobal> faMatrixGlobals = UploadEmployeeData.getBackupExcel(file.getInputStream());

            Map<String, List<Long>> trainingToMatricules = new HashMap<>();

            for (FaMatrixGlobal faMatrixGlobal : faMatrixGlobals) {
                Long matricule = faMatrixGlobal.getMatricule();
                List<String> trainings = faMatrixGlobal.getTrainings();

                for (String training : trainings) {
                    trainingToMatricules.computeIfAbsent(training, k -> new ArrayList<>()).add(matricule);
                }
            }

            List<FaMatrixGlobalTraining> faMatrixGlobalTrainings = new ArrayList<>();
            for (Map.Entry<String, List<Long>> entry : trainingToMatricules.entrySet()) {
                faMatrixGlobalTrainings.add(new FaMatrixGlobalTraining(entry.getKey(), entry.getValue()));
            }
            for (FaMatrixGlobalTraining faMatrixGlobalTraining : faMatrixGlobalTrainings) {
                printFaMatrixGlobalTrainingDetails(faMatrixGlobalTraining);

                Training training = createTrainingFromFaMatrixGlobalTraining(faMatrixGlobalTraining);
                training = trainingRepo.save(training);

                for (Long matricule : faMatrixGlobalTraining.getMatricules()) {
                    Employee employee = employeeRepo.findByMatricule(matricule);
                    if (employee != null) {
                        assignTrainingToEmployee(employee, training, faMatrixGlobalTraining.getTraining());
                    }
                }
            }

        }
    }

    private void printFaMatrixGlobalTrainingDetails(FaMatrixGlobalTraining faMatrixGlobalTraining) {
        System.out.println("Training: " + faMatrixGlobalTraining.getTraining());
        System.out.println("Matricules: " + faMatrixGlobalTraining.getMatricules());
        System.out.println(faMatrixGlobalTraining.getMatricules().size());
        System.out.println();
    }

    private Training createTrainingFromFaMatrixGlobalTraining(FaMatrixGlobalTraining faMatrixGlobalTraining) {
        Training training = new Training();
        training.setTrainingId(utils.getGeneratedId(22));
        training.setTrainingType(trainingTypeRepo.findByTtName("Process"));
        TrainingTitle trainingTitle = trainingTitleRepo.findByTrainingTitleName(faMatrixGlobalTraining.getTraining());
        training.setTrainingTitle(trainingTitle);
        training.setModalite("Présentielle");
        training.setDureeParHeure(8);
        training.setDateDebut(new Date(122, Calendar.JANUARY, 1));
        training.setDateFin(new Date(122, Calendar.JANUARY, 1));
        training.setEva(false);
        training.setFormatteur("KHATRI Abdessalam");
        training.setPrestataire("APTIV");
        return training;
    }

    private void assignTrainingToEmployee(Employee employee, Training training, String trainingName) {
        boolean found = employee.getTrainings().stream()
                .anyMatch(tf -> Objects.equals(tf.getTrainingTitle().getTrainingTitleName(), trainingName));
        if (!found) {
            List<Training> trainings = employee.getTrainings();
            trainings.add(training);
            employee.setTrainings(trainings);
            employeeRepo.save(employee);
        }
    }

    @Override
    public void saveTrainingDataToDb(MultipartFile file) {
        if (UploadEmployeeData.isValidFormat(file)) {
            try {
                List<TrainingFromExcel> trainingFromExcels =
                        UploadEmployeeData.getTrainingDataFromExcel(file.getInputStream());
                List<TrainingDataFormatter> trainingDataFormatters = formatData(trainingFromExcels);

                trainingDataFormatters.stream()
                        .filter(tfe -> tfe.getTrainingTitle() != null && tfe.getTrainingType() != null && tfe.getModalite() != null)
                        .map(tfe -> {
                            List<Training> existingTrainings =
                                    trainingRepo.findAllByTrainingTypeTtNameAndTrainingTitleTrainingTitleNameAndDateDebutBetween(
                                            tfe.getTrainingType(), tfe.getTrainingTitle(), tfe.getDdb(), tfe.getDdf());
                            if (existingTrainings.isEmpty()) {
                                return createNewTraining(tfe);
                            } else {
                                return mergeExistingTrainings(existingTrainings, tfe);
                            }
                        })
                        .forEach(trainingRepo::save);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Training mergeExistingTrainings(List<Training> existingTrainings, TrainingDataFormatter tfe) {
        Training mergedTraining = new Training();
        mergedTraining.setTrainingId(utils.getGeneratedId(22));
        mergedTraining.setTrainingType(existingTrainings.get(0).getTrainingType());
        mergedTraining.setTrainingTitle(existingTrainings.get(0).getTrainingTitle());
        mergedTraining.setModalite(existingTrainings.get(0).getModalite());
        mergedTraining.setDureeParHeure(existingTrainings.get(0).getDureeParHeure());
        mergedTraining.setDateDebut(existingTrainings.get(0).getDateDebut());
        mergedTraining.setDateFin(existingTrainings.get(0).getDateFin());
        mergedTraining.setEva(existingTrainings.get(0).isEva());
        mergedTraining.setFormatteur(existingTrainings.get(0).getFormatteur());
        mergedTraining.setPrestataire(existingTrainings.get(0).getPrestataire());

        Set<Employee> allEmployeesSet = new HashSet<>();
        existingTrainings.forEach(training -> allEmployeesSet.addAll(training.getEmployees()));

        List<Employee> newEmployees = tfe.getMatricules().stream()
                .map(employeeRepo::findByMatricule)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        allEmployeesSet.addAll(newEmployees);

        List<Employee> allEmployees = new ArrayList<>(allEmployeesSet);
        mergedTraining.setEmployees(allEmployees);

        // Delete the existing trainings
        existingTrainings.forEach(training -> {
            training.getEmployees().clear();
            trainingRepo.delete(training);
        });

        return mergedTraining;
    }

    private Training createNewTraining(TrainingDataFormatter tfe) {
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
        List<Employee> employees = tfe.getMatricules().stream()
                .map(employeeRepo::findByMatricule)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        training.setEmployees(employees);
        return training;
    }

    private void updateExistingTraining(TrainingDataFormatter tfe, Training trainingf) {
        if (trainingf.getEmployees().size() != tfe.getMatricules().size()) {
            List<Employee> employees = tfe.getMatricules().stream()
                    .map(employeeRepo::findByMatricule)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            trainingf.setEmployees(employees);
            trainingRepo.save(trainingf);
        }
    }

    public List<TrainingDataFormatter> formatData(List<TrainingFromExcel> trainingFromExcels) {
        List<TrainingDataFormatter> trainingDataFormatters = new ArrayList<>();

        trainingFromExcels.forEach(tfe -> {
            if (isValidTraining(tfe)) {
                boolean found = trainingDataFormatters.stream()
                        .anyMatch(tf -> Objects.equals(tf.getTrainingTitle().trim(), tfe.getTrainingTitle().trim()) &&
                                Objects.equals(tf.getTrainingType().trim(), tfe.getTrainingType().trim()) &&
                                tf.getDdb().compareTo(tfe.getDdb()) == 0 &&
                                tf.getDdf().compareTo(tfe.getDdf()) == 0);

                if (found) {
                    trainingDataFormatters.stream()
                            .filter(tf -> Objects.equals(tf.getTrainingTitle().trim(), tfe.getTrainingTitle().trim()) &&
                                    Objects.equals(tf.getTrainingType().trim(), tfe.getTrainingType().trim()) &&
                                    tf.getDdb().compareTo(tfe.getDdb()) == 0 &&
                                    tf.getDdf().compareTo(tfe.getDdf()) == 0)
                            .findFirst()
                            .ifPresent(tf -> tf.getMatricules().add(tfe.getMatricule()));
                } else {
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
        });

        return trainingDataFormatters;
    }

    private boolean isValidTraining(TrainingFromExcel tfe) {
        return tfe.getDdb() != null && tfe.getDdf() != null && tfe.getDph() != null;
    }

    @Override
    public TrainingDataFormatter addTrainingToEmployees(TrainingDataFormatter trainingDataFormatter) {
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
            training = trainingRepo.save(training);
            trainingDataFormatter.setTrainingId(training.getTrainingId());
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
            Training training = trainingRepo.save(trainingf);
            trainingDataFormatter.setTrainingId(training.getTrainingId());
        }
        return trainingDataFormatter;
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
