package com.aptiv.trainig_tracker.repositories;

import com.aptiv.trainig_tracker.domain.OrderQualification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderRepo extends JpaRepository<OrderQualification, Long> {
    OrderQualification findByOrderId(String orderId);
    List<OrderQualification> findAllByOrderDateBetween(Date startDate, Date endDate);
}
