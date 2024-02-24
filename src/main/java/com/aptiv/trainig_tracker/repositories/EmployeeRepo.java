package com.aptiv.trainig_tracker.repositories;

import com.aptiv.trainig_tracker.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee, Long> {
}
