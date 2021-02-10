package com.afridi.gamingbackend.controller;


import com.afridi.gamingbackend.dto.request.ChangePasswordRequest;
import com.afridi.gamingbackend.dto.request.LoginForm;
import com.afridi.gamingbackend.services.SignUpAndSignInService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Slf4j
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class CommonUsersAuthLog {

    @Autowired
    private SignUpAndSignInService signUpAndSignInService;


    @PostMapping("/common/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginForm loginRequest) {

        return ResponseEntity.ok(signUpAndSignInService.CommonUserBothSignIn(loginRequest));
    }

    @GetMapping("/common/users/log")
    public String getLoggedAuthId() {
        return signUpAndSignInService.getLoggedAuthUser();
    }


    @PutMapping("/common/password/reset")
    public String updatePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {

        return signUpAndSignInService.updatePasswordCommon(changePasswordRequest);
    }

}
