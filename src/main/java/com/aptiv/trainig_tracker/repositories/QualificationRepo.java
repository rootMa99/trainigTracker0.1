package com.aptiv.trainig_tracker.repositories;

import com.aptiv.trainig_tracker.domain.Qualification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QualificationRepo extends JpaRepository<Qualification, Long> {
}
