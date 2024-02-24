package com.aptiv.trainig_tracker.repositories;

import com.aptiv.trainig_tracker.domain.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepo extends JpaRepository<Department, Long> {
    Department findDepartmentName(String name);
}
