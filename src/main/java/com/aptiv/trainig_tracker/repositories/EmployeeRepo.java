package com.aptiv.trainig_tracker.repositories;

import com.aptiv.trainig_tracker.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee, Long> {
    Employee findByMatricule(long matricule);

    List<Employee> findByMatriculeIn(List<Long> matricules);
}
