package com.aptiv.trainig_tracker.repositories;

import com.aptiv.trainig_tracker.domain.Role;
import com.aptiv.trainig_tracker.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String name);
    User findByRole(Role role);
}
