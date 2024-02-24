package com.aptiv.trainig_tracker.repositories;

import com.aptiv.trainig_tracker.domain.Family;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FamilyRepo extends JpaRepository<Family, Long> {
    Family findByFamilyName(String name);
}
