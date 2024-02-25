package com.aptiv.trainig_tracker.repositories;

import com.aptiv.trainig_tracker.domain.TrainingTitle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingTitleRepo extends JpaRepository<TrainingTitle, Long> {
    TrainingTitle findByTTitleName(String name);
}
