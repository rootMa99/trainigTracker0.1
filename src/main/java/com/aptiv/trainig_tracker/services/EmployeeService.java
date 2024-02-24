package com.aptiv.trainig_tracker.services;

import org.springframework.web.multipart.MultipartFile;

public interface EmployeeService {
    void saveEmployeeDataToDb(MultipartFile file) throws IllegalAccessException;
}
