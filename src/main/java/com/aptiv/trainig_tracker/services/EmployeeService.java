package com.aptiv.trainig_tracker.services;

import com.aptiv.trainig_tracker.models.CrewDto;
import com.aptiv.trainig_tracker.models.EmployeeModel;
import org.springframework.web.multipart.MultipartFile;

public interface EmployeeService {
    void saveEmployeeDataToDb(MultipartFile file) throws IllegalAccessException;
    EmployeeModel getEmployeeData(long matricule);

    CrewDto getCrewName(String crewName);

}
