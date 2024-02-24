package com.aptiv.trainig_tracker.repositories;

import com.aptiv.trainig_tracker.domain.Poste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PosteRepo extends JpaRepository<Poste, Long> {
    Poste findByPosteName(String name);
}
