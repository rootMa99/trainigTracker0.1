package com.aptiv.trainig_tracker.services.serviceImpl;

import com.aptiv.trainig_tracker.domain.Employee;
import com.aptiv.trainig_tracker.domain.OrderQualification;
import com.aptiv.trainig_tracker.models.*;
import com.aptiv.trainig_tracker.repositories.*;
import com.aptiv.trainig_tracker.services.OtherService;
import com.aptiv.trainig_tracker.ui.Utils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class OtherServiceImpl implements OtherService {
    //CategoryRepo categoryRepo;
    //CoordinatorRepo coordinatorRepo;
    //CrewRepo crewRepo;
    //DepartmentRepo departmentRepo;
    //FamilyRepo familyRepo;
    //PosteRepo posteRepo;
    //TeamLeaderRepo teamLeaderRepo;
    //TrainingTypeRepo trainingTypeRepo;
    //TrainingRepo trainingRepo;
    Utils utils;
    EmployeeRepo employeeRepo;
    ShiftLeaderRepo shiftLeaderRepo;
    TrainingTitleRepo trainingTitleRepo;
    OrderRepo orderRepo;

    @Override
    public StatusRest saveOrderToDb(List<OrderDto> orderDtoList) {
        List<OrderQualification> orderQualifications = new ArrayList<>();
        StatusRest statusRest = new StatusRest();
        List<Long> notFound = new ArrayList<>();
        List<QualificationRest> qualificationRests = new ArrayList<>();
        for (OrderDto o : orderDtoList) {
            QualificationRest qr = new QualificationRest();
            List<EmployeeRest> employeeRests = new ArrayList<>();
            OrderQualification orderQualification = new OrderQualification();
            orderQualification.setOrderId(utils.getGeneratedId(22));
            orderQualification.setShift(o.getShift());
            orderQualification.setShiftLeader(shiftLeaderRepo.findByName(o.getShiftLeaderName()));
            orderQualification.setTrainingTitle(trainingTitleRepo.findByTrainingTitleName(o.getQualification()));
            orderQualification.setOrderDate(o.getOrderdate());
            orderQualification.setOrderDateSubmit(new Date());
            List<Employee> employees = new ArrayList<>();
            for (Long l : o.getMatricules()) {
                Employee e = employeeRepo.findByMatricule(l);
                if (e == null) {
                    notFound.add(l);
                } else {
                    employees.add(e);
                    employeeRests.add(getEmployeeRest(e));
                }
            }
            orderQualification.setEmployees(employees);
            orderQualifications.add(orderQualification);
            qr.setQualificationId(orderQualification.getOrderId());
            qr.setEmployeeRests(employeeRests);
            qr.setQualification(o.getQualification());
            qr.setQualificationDate(o.getOrderdate());
            qualificationRests.add(qr);
        }
        orderRepo.saveAll(orderQualifications);
        statusRest.setNotFound(notFound);
        statusRest.setQualificationRests(qualificationRests);
        return statusRest;
    }

    private static EmployeeRest getEmployeeRest(Employee e) {
        EmployeeRest employeeRest = new EmployeeRest();
        employeeRest.setMatricule(e.getMatricule());
        employeeRest.setNom(e.getNom());
        employeeRest.setPrenom(e.getPrenom());
        employeeRest.setCategory(e.getCategory().getCategoryName());
        employeeRest.setFonction(e.getFonctionEntreprise());
        employeeRest.setDepartment(e.getDepartment().getDepartmentName());
        employeeRest.setPoste(e.getPoste().getPosteName());
        employeeRest.setCrew(e.getCrew().getCrewName());
        employeeRest.setFamily(e.getFamily().getFamilyName());
        employeeRest.setCoordinator(e.getCoordinator().getName());
        employeeRest.setShiftLeader(e.getShiftLeader().getName());
        employeeRest.setTeamLeader(e.getTeamLeader().getName());
        return employeeRest;
    }

    @Override
    public OrderRest getOderById(String orderId) {
        OrderRest orderRest = new OrderRest();
        OrderQualification order = orderRepo.findByOrderId(orderId);
        orderRest.setQualificationId(order.getOrderId());
        orderRest.setQualification(order.getTrainingTitle().getTrainingTitleName());
        orderRest.setShiftLeader(order.getShiftLeader().getName());
        orderRest.setQualificationDate(order.getOrderDate());
        orderRest.setSubmitDate(order.getOrderDateSubmit());
        List<EmployeeRest> ers = new ArrayList<>();
        for (Employee e : order.getEmployees()) {
            ers.add(getEmployeeRest(e));
        }
        orderRest.setEmployeeRests(ers);
        return orderRest;
    }
}
