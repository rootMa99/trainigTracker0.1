package com.aptiv.trainig_tracker.repositories;

import com.aptiv.trainig_tracker.domain.OrderQualification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepo extends JpaRepository<OrderQualification, Long> {
}
