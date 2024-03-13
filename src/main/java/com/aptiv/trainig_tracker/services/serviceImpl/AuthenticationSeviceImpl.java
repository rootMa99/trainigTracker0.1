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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthenticationSeviceImpl implements AuthenticationService {
    private final UserRepo userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;


    @Override
    public User changePassword(ChangePwd changePwd) {
        User user = userRepository.findByRole(Role.ROOT);
        user.setPassword(new BCryptPasswordEncoder().encode(changePwd.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User createSl(SignInRequest signInRequest) {
        return createUser(signInRequest, Role.SHIFT_LEADER);
    }

    private User createUser(SignInRequest signInRequest, Role role) {
        User userRoot = new User();
        boolean userN = userRepository.findByUserName(signInRequest.getUserName()).isPresent();
        if (!userN) {
            userRoot.setUserName(signInRequest.getUserName());
            userRoot.setPassword(new BCryptPasswordEncoder().encode(signInRequest.getPassword()));
            userRoot.setRole(role);
            userRepository.save(userRoot);
            return userRoot;
        }
        throw new RuntimeException("An account with these credentials has previously been registered in the database");
    }

    @Override
    public User createAdmin(SignInRequest signInRequest) {
        return createUser(signInRequest, Role.ADMIN);
    }

    @Override
    public User createTrainer(SignInRequest signInRequest) {
        return createUser(signInRequest, Role.TRAINER);
    }

    @Override
    public JwtAuthenticationResponse signIn(SignInRequest signInRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getUserName(),
                signInRequest.getPassword()));
        var user =
                userRepository.findByUserName(signInRequest.getUserName()).orElseThrow(() -> new UsernameNotFoundException("invalid userName or password"));
        var jwt = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);
        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
        jwtAuthenticationResponse.setToken(jwt);
        jwtAuthenticationResponse.setRefreshToken(refreshToken);
        jwtAuthenticationResponse.setRole(user.getRole().name());
        jwtAuthenticationResponse.setUserName(user.getUsername());
        return jwtAuthenticationResponse;
    }

    @Override
    public JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String userName = jwtService.extractUserName(refreshTokenRequest.getToken());
        User user = userRepository.findByUserName(userName).orElseThrow();
        if (jwtService.isTokenValid(refreshTokenRequest.getToken(), user)) {
            var jwt = jwtService.generateToken(user);
            JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
            jwtAuthenticationResponse.setToken(jwt);
            jwtAuthenticationResponse.setRefreshToken(refreshTokenRequest.getToken());
            return jwtAuthenticationResponse;
        }
        return null;
    }
}
