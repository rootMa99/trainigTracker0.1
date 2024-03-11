package com.aptiv.trainig_tracker.services.serviceImpl;

import com.aptiv.trainig_tracker.domain.Role;
import com.aptiv.trainig_tracker.domain.User;
import com.aptiv.trainig_tracker.models.ChangePwd;
import com.aptiv.trainig_tracker.models.JwtAuthenticationResponse;
import com.aptiv.trainig_tracker.models.RefreshTokenRequest;
import com.aptiv.trainig_tracker.models.SignInRequest;
import com.aptiv.trainig_tracker.repositories.UserRepo;
import com.aptiv.trainig_tracker.services.AuthenticationService;
import com.aptiv.trainig_tracker.services.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationSeviceImpl implements AuthenticationService {
    private final UserRepo userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;


    @Override
    public User changePassword(ChangePwd changePwd) {
        User user=userRepository.findByRole(Role.ROOT);
        user.setPassword(new BCryptPasswordEncoder().encode(changePwd.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public JwtAuthenticationResponse signIn(SignInRequest signInRequest) {
        return null;
    }

    @Override
    public JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        return null;
    }
}
