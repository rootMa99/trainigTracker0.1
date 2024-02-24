package com.aptiv.trainig_tracker.repositories;

import com.aptiv.trainig_tracker.domain.TeamLeader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamLeaderRepo extends JpaRepository<TeamLeader, Long> {
    TeamLeader findByName(String name);

}
