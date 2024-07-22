package com.aptiv.trainig_tracker.repositories;

import com.aptiv.trainig_tracker.domain.Qualification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QualificationRepo extends JpaRepository<Qualification, Long> {
    Qualification findByName(String name);
}
