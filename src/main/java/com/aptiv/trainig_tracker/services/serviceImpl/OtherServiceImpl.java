package com.aptiv.trainig_tracker.services.serviceImpl;

import com.aptiv.trainig_tracker.models.OrderDto;
import com.aptiv.trainig_tracker.models.StatusRest;
import com.aptiv.trainig_tracker.repositories.*;
import com.aptiv.trainig_tracker.services.OtherService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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
    EmployeeRepo employeeRepo;
    ShiftLeaderRepo shiftLeaderRepo;
    TrainingTitleRepo trainingTitleRepo;

    public StatusRest saveOrderToDb(List<OrderDto> orderDtoList){
        return null;
    }
}
