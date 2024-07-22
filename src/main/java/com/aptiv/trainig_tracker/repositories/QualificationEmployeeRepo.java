package com.aptiv.trainig_tracker.repositories;

import com.aptiv.trainig_tracker.domain.QualificationEmployee;
import com.aptiv.trainig_tracker.domain.QualificationEmployeeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QualificationEmployeeRepo extends JpaRepository<QualificationEmployee, QualificationEmployeeId> {
}
