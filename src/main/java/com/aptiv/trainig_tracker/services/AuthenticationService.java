package com.aptiv.trainig_tracker.services;

import com.aptiv.trainig_tracker.domain.User;
import com.aptiv.trainig_tracker.models.JwtAuthenticationResponse;
import com.aptiv.trainig_tracker.models.RefreshTokenRequest;
import com.aptiv.trainig_tracker.models.SignInRequest;

public interface AuthenticationService {
    void changePassword(String changePwd);

    User createSl(SignInRequest signInRequest);

    User createAdmin(SignInRequest signInRequest);

    User createTrainer(SignInRequest signInRequest);

    JwtAuthenticationResponse signIn(SignInRequest signInRequest);
    JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
}
