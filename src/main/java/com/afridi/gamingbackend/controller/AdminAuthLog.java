package com.afridi.gamingbackend.controller;



import com.afridi.gamingbackend.dto.request.AdminInfoRequest;
import com.afridi.gamingbackend.dto.request.UserSignUpForm;
import com.afridi.gamingbackend.dto.response.AdminInfoResponse;
import com.afridi.gamingbackend.services.AdminInfoService;
import com.afridi.gamingbackend.services.SignUpAndSignInService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class AdminAuthLog {

    private SignUpAndSignInService signUpAndSignInService;
    private AdminInfoService adminInfoService;


    @PostMapping("/admin/signup")
    public ResponseEntity<String> registerUser(@RequestBody UserSignUpForm signUpRequest) {
        return signUpAndSignInService.AdminSignUp(signUpRequest);
    }



    @PostMapping("/admin/create/profile")
    public ResponseEntity<String> createAdmin(@RequestBody AdminInfoRequest adminInfoRequest) {
        return adminInfoService.createAdminInformation(adminInfoRequest);
    }


    @GetMapping("/admin/show/profile")
    public List<AdminInfoResponse> getProfile() {
        return adminInfoService.getAdminInformation();
    }
}
