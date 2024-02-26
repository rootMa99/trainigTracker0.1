package com.aptiv.trainig_tracker.services.serviceImpl;

import com.aptiv.trainig_tracker.domain.*;
import com.aptiv.trainig_tracker.models.DataExcelEmployee;
import com.aptiv.trainig_tracker.models.EmployeeModel;
import com.aptiv.trainig_tracker.models.TrainingFromExcel;
import com.aptiv.trainig_tracker.repositories.*;
import com.aptiv.trainig_tracker.services.EmployeeService;
import com.aptiv.trainig_tracker.services.UploadEmployeeData;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    CategoryRepo categoryRepo;
    CoordinatorRepo coordinatorRepo;
    CrewRepo crewRepo;
    DepartmentRepo departmentRepo;
    EmployeeRepo employeeRepo;
    FamilyRepo familyRepo;
    PosteRepo posteRepo;
    ShiftLeaderRepo shiftLeaderRepo;
    TeamLeaderRepo teamLeaderRepo;

    @Override
    public EmployeeModel getEmployeeData(long matricule) {
        Employee employee= employeeRepo.findByMatricule(matricule);
        EmployeeModel employeeModel=new EmployeeModel();
        if (employee!=null){
            employeeModel.setMatricule(employee.getMatricule());
            employeeModel.setNom(employee.getNom());
            employeeModel.setPrenom(employee.getPrenom());
            employeeModel.setCategory(employee.getCategory().getCategoryName());
            employeeModel.setFonction(employee.getFonctionEntreprise());
            employeeModel.setDepartment(employee.getDepartment().getDepartmentName());
            employeeModel.setPoste(employee.getPoste().getPosteName());
            employeeModel.setCrew(employee.getCrew().getCrewName());
            employeeModel.setFamily(employee.getFamily().getFamilyName());
            employeeModel.setCoordinator(employee.getCoordinator().getName());
            employeeModel.setShiftLeader(employee.getShiftLeader().getName());
            employeeModel.setTeamLeader(employee.getTeamLeader().getName());
            List<TrainingFromExcel>trainingFromExcels= new ArrayList<>();
            for (Training t: employee.getTrainings()){
                TrainingFromExcel trainingFromExcel = getTrainingFromExcel(t);
                trainingFromExcels.add(trainingFromExcel);
            }
            employeeModel.setTrainingFromExcels(trainingFromExcels);
        }
        return employeeModel;
    }

    private static TrainingFromExcel getTrainingFromExcel(Training t) {
        TrainingFromExcel trainingFromExcel=new TrainingFromExcel();
        trainingFromExcel.setTrainingId(t.getTrainingId());
        trainingFromExcel.setTrainingTitle(t.getTrainingTitle().getTrainingTitleName());
        trainingFromExcel.setTrainingType(t.getTrainingType().getTtName());
        trainingFromExcel.setModalite(t.getModalite());
        trainingFromExcel.setDph(t.getDureeParHeure());
        trainingFromExcel.setDdb(t.getDateDebut());
        trainingFromExcel.setDdf(t.getDateFin());
        trainingFromExcel.setPrestataire(t.getPrestataire());
        trainingFromExcel.setFormatteur(t.getFormatteur());
        trainingFromExcel.setEva(t.isEva());
        return trainingFromExcel;
    }

    @Override
    public void saveEmployeeDataToDb(MultipartFile file) throws IllegalAccessException {
        ModelMapper mp=new ModelMapper();
        mp.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        if (UploadEmployeeData.isValidFormat(file)){
            try {
                List<DataExcelEmployee>dataExcelEmployees= UploadEmployeeData.getEmployeesDataFromExcel(file.getInputStream());
                List<Employee>employees=new ArrayList<>();
                for (DataExcelEmployee dee : dataExcelEmployees){
                    Employee employee=new Employee();
                    employee.setMatricule(dee.getMatricule());
                    employee.setNom(dee.getNom());
                    employee.setPrenom(dee.getPrenom());
                    employee.setFonctionEntreprise(dee.getFonction());
                    Category category= categoryRepo.findByCategoryName(dee.getCategory());
                    if (category==null){
                        Category ct= new Category();
                        ct.setCategoryName(dee.getCategory());
                        category= categoryRepo.save(ct);
                    }
                    employee.setCategory(category);
                    Crew crew= crewRepo.findByCrewName(dee.getCrew());
                    if (crew==null){
                        Crew cr= new Crew();
                        cr.setCrewName(dee.getCrew());
                        crew= crewRepo.save(cr);
                    }
                    employee.setCrew(crew);
                    Poste poste= posteRepo.findByPosteName(dee.getPoste());
                    if (poste==null){
                        Poste ps=new Poste();
                        ps.setPosteName(dee.getPoste());
                        poste= posteRepo.save(ps);
                    }
                    employee.setPoste(poste);
                    Family family=familyRepo.findByFamilyName(dee.getFamily());
                    if (family==null){
                        Family fm=new Family();
                        fm.setFamilyName(dee.getFamily());
                        family=familyRepo.save(fm);
                    }
                    employee.setFamily(family);
                    Department department= departmentRepo.findByDepartmentName(dee.getDepartment());
                    if (department==null){
                        Department dprt=new Department();
                        dprt.setDepartmentName(dee.getDepartment());
                        department=departmentRepo.save(dprt);
                    }
                    employee.setDepartment(department);
                    Coordinator coordinator= coordinatorRepo.findByName(dee.getCoordinator());
                    if (coordinator==null){
                        Coordinator c=new Coordinator();
                        c.setName(dee.getCoordinator());
                        coordinator= coordinatorRepo.save(c);
                    }
                    employee.setCoordinator(coordinator);
                    ShiftLeader shiftLeader= shiftLeaderRepo.findByName(dee.getShiftLeader());
                    if (shiftLeader==null){
                        ShiftLeader sl= new ShiftLeader();
                        sl.setName(dee.getShiftLeader());
                        shiftLeader= shiftLeaderRepo.save(sl);
                    }
                    employee.setShiftLeader(shiftLeader);
                    TeamLeader teamLeader= teamLeaderRepo.findByName(dee.getTeamLeader());
                    if (teamLeader==null){
                        TeamLeader tl= new TeamLeader();
                        tl.setName(dee.getTeamLeader());
                        teamLeader= teamLeaderRepo.save(tl);
                    }
                    employee.setTeamLeader(teamLeader);
                    employees.add(employee);
                }
                employeeRepo.saveAll(employees);
            }catch (IOException e){
                throw new RuntimeException(e);
            }
        }
    }
}
