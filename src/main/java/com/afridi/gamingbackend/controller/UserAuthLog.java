package com.afridi.gamingbackend.controller;



import com.afridi.gamingbackend.dto.request.UserInfoRequest;
import com.afridi.gamingbackend.dto.request.UserSignUpForm;
import com.afridi.gamingbackend.dto.response.UserInfoResponse;
import com.afridi.gamingbackend.services.SignUpAndSignInService;
import com.afridi.gamingbackend.services.UserInfoService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class UserAuthLog {


    private SignUpAndSignInService signUpAndSignInService;
    private UserInfoService userInfoService;

    @PostMapping("/user/signup")
    public ResponseEntity<String> registerUser(@RequestBody UserSignUpForm signUpRequest) {
        return signUpAndSignInService.CommonUserSignUp(signUpRequest);
    }

    @PostMapping("/user/create/profile")
    public ResponseEntity<String> createAdmin(@RequestBody UserInfoRequest userInfoRequest) {
        return userInfoService.createUserInformation(userInfoRequest);
    }

    @GetMapping("/user/show/profile")
    public ResponseEntity<UserInfoResponse> getProfile() {
        return userInfoService.getUserInformation();
    }

}
