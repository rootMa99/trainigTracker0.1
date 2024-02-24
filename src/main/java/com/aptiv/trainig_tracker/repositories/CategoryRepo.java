package com.aptiv.trainig_tracker.repositories;

import com.aptiv.trainig_tracker.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Long> {
    Category findByCategoryName(String name);
}
