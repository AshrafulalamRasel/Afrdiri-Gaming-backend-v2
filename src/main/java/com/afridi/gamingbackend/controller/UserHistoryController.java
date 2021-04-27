package com.afridi.gamingbackend.controller;


import com.afridi.gamingbackend.dto.request.UserInfoRequest;
import com.afridi.gamingbackend.dto.request.UserSignUpForm;
import com.afridi.gamingbackend.dto.response.UserHistoryResponse;
import com.afridi.gamingbackend.dto.response.UserInfoResponse;
import com.afridi.gamingbackend.services.HistroryServices;
import com.afridi.gamingbackend.services.SignUpAndSignInService;
import com.afridi.gamingbackend.services.UserInfoService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class UserHistoryController {

    private HistroryServices histroryServices;

    @GetMapping("/show/history")
    public List<UserHistoryResponse> getHistoryProfile() {
        return histroryServices.getHistoryByUserLoggedId();
    }

}
