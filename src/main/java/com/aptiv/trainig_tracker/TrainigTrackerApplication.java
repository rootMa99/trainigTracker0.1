package com.aptiv.trainig_tracker;

import com.aptiv.trainig_tracker.domain.Role;
import com.aptiv.trainig_tracker.domain.User;
import com.aptiv.trainig_tracker.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class TrainigTrackerApplication implements CommandLineRunner {
    @Autowired
    UserRepo userRepo;

    public static void main(String[] args) {
        SpringApplication.run(TrainigTrackerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        User user=userRepo.findByRole(Role.ROOT);
        if (user==null){
            User userRoot=new User();
            userRoot.setUserName("root");
            userRoot.setPassword(new BCryptPasswordEncoder().encode("AptivRoot"));
            userRoot.setRole(Role.ROOT);
            userRepo.save(userRoot);
        }
    }
}
