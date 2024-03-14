package com.aptiv.trainig_tracker.controllers;

import com.aptiv.trainig_tracker.domain.User;
import com.aptiv.trainig_tracker.models.SignInRequest;
import com.aptiv.trainig_tracker.services.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/root")
@AllArgsConstructor
public class RootController {

    private AuthenticationService authenticationService;
    
    @PostMapping("/createSl")
    public ResponseEntity<User> createSl(@RequestBody SignInRequest signInRequest){
        return ResponseEntity.ok(authenticationService.createSl(signInRequest));
    }

    @PostMapping("/createAdmin")
    public ResponseEntity<User> createAdmin(@RequestBody SignInRequest signInRequest){
        return ResponseEntity.ok(authenticationService.createAdmin(signInRequest));
    }
    @PostMapping("/createTrainer")
    public ResponseEntity<User> createTrainer(@RequestBody SignInRequest signInRequest){
        return ResponseEntity.ok(authenticationService.createTrainer(signInRequest));
    }

}
