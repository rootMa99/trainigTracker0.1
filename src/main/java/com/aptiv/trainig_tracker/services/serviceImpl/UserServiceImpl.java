package com.aptiv.trainig_tracker.services.serviceImpl;

import com.aptiv.trainig_tracker.repositories.UserRepo;
import com.aptiv.trainig_tracker.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private  final UserRepo userRepo;
    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return userRepo.findByUserName(username).orElseThrow(()->new UsernameNotFoundException("no user " +
                        "found"));
            }
        };
    }
}
