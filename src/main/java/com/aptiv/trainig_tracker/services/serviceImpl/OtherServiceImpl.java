package com.aptiv.trainig_tracker.services.serviceImpl;

import com.aptiv.trainig_tracker.domain.Employee;
import com.aptiv.trainig_tracker.domain.OrderQualification;
import com.aptiv.trainig_tracker.models.OrderDto;
import com.aptiv.trainig_tracker.models.StatusRest;
import com.aptiv.trainig_tracker.repositories.*;
import com.aptiv.trainig_tracker.services.OtherService;
import com.aptiv.trainig_tracker.ui.Utils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    @Override
    public StatusRest saveOrderToDb(List<OrderDto> orderDtoList) {
        List<OrderQualification> orderQualifications = new ArrayList<>();
        StatusRest statusRest = new StatusRest();
        List<Long> notFound = new ArrayList<>();
        for (OrderDto o : orderDtoList) {
            OrderQualification orderQualification = new OrderQualification();
            orderQualification.setOrderId(utils.getGeneratedId(22));
            orderQualification.setShift(o.getShift());
            orderQualification.setShiftLeader(shiftLeaderRepo.findByName(o.getShiftLeaderName()));
            orderQualification.setTrainingTitle(trainingTitleRepo.findByTrainingTitleName(o.getQualification()));
            orderQualification.setOrderDate(o.getOrderdate());
            List<Employee> employees = new ArrayList<>();
            for (Long l : o.getMatricules()) {
                Employee e = employeeRepo.findByMatricule(l);
                if (e == null) {
                    notFound.add(l);
                } else {
                    employees.add(e);
                }
            }
            orderQualification.setEmployees(employees);
            orderQualifications.add(orderQualification);
        }
        statusRest.setNotFound(notFound);
        return statusRest;
    }
}
