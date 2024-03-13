package com.aptiv.trainig_tracker.services;

import com.aptiv.trainig_tracker.domain.User;
import com.aptiv.trainig_tracker.models.ChangePwd;
import com.aptiv.trainig_tracker.models.JwtAuthenticationResponse;
import com.aptiv.trainig_tracker.models.RefreshTokenRequest;
import com.aptiv.trainig_tracker.models.SignInRequest;

public interface AuthenticationService {
    User changePassword(ChangePwd changePwd);

    User createSl(SignInRequest signInRequest);

    User createAdmin(SignInRequest signInRequest);

    JwtAuthenticationResponse signIn(SignInRequest signInRequest);
    JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
}
