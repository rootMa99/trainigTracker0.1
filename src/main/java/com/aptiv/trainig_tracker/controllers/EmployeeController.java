package com.aptiv.trainig_tracker.controllers;

import com.aptiv.trainig_tracker.models.EmployeeModel;
import com.aptiv.trainig_tracker.services.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/employee")
@AllArgsConstructor
public class EmployeeController {
    EmployeeService employeeService;

    @GetMapping(path ="/employee" )
    public EmployeeModel getEmployeeData(@RequestParam long matricule){
        return employeeService.getEmployeeData(matricule);
    }
}
