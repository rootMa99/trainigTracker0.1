package com.aptiv.trainig_tracker.controllers;

import com.aptiv.trainig_tracker.domain.User;
import com.aptiv.trainig_tracker.models.SignInRequest;
import com.aptiv.trainig_tracker.models.UserRest;
import com.aptiv.trainig_tracker.services.AuthenticationService;
import com.aptiv.trainig_tracker.services.OtherService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/root")
@AllArgsConstructor
public class RootController {
    OtherService otherService;
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
    @GetMapping("/data/shiftleaders")
    public List<String> getShiftLeaders(){
        return otherService.getShiftLeaders();
    }
    @GetMapping("/data/users")
    public List<UserRest> getUsers(){
        return otherService.getUserRest();
    }

    @PostMapping("/data/updateUserPwd")
    public void updatePwd(@RequestParam String userName, @RequestParam String password){
        otherService.changePwd(userName, password);
    }
    @PostMapping("/data/updateRootPwd")
    public void updateRootPwd(@RequestParam String password){
       authenticationService.changePassword(password);
    }


}
