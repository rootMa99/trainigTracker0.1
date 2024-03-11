package com.aptiv.trainig_tracker.models;

import lombok.Data;

@Data
public class SignInRequest {
    private String userName;
    private String password;
}
