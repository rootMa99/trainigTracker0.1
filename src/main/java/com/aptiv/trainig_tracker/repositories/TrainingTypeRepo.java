package com.aptiv.trainig_tracker.repositories;

import com.aptiv.trainig_tracker.domain.TrainingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingTypeRepo extends JpaRepository<TrainingType, Long> {
}
