package com.aptiv.trainig_tracker.repositories;

import com.aptiv.trainig_tracker.domain.Coordinator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoordinatorRepo extends JpaRepository<Coordinator, Long> {
}
