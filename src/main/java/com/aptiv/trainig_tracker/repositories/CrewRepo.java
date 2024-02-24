package com.aptiv.trainig_tracker.repositories;

import com.aptiv.trainig_tracker.domain.Crew;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrewRepo extends JpaRepository<Crew, Long> {

    Crew findByCrewName(String name);

}
