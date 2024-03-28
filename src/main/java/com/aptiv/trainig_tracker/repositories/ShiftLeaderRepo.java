package com.aptiv.trainig_tracker.repositories;

import com.aptiv.trainig_tracker.domain.ShiftLeader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface ShiftLeaderRepo extends JpaRepository<ShiftLeader, Long> {
    ShiftLeader findByName(String name);
    ShiftLeader findByNameAndOrderQualificationsOrderDateBetween(String name, Date ds, Date ed);
}
