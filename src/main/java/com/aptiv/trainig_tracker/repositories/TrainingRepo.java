package com.aptiv.trainig_tracker.repositories;

import com.aptiv.trainig_tracker.domain.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingRepo extends JpaRepository<Training, Long> {
}
