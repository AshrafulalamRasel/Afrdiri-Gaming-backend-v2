package com.afridi.gamingbackend.controller;



import com.afridi.gamingbackend.dto.request.AdminInfoRequest;
import com.afridi.gamingbackend.dto.request.PlayStoreLInkRequest;
import com.afridi.gamingbackend.dto.request.UserSignUpForm;
import com.afridi.gamingbackend.dto.response.AdminInfoResponse;
import com.afridi.gamingbackend.dto.response.IdentityResponse;
import com.afridi.gamingbackend.services.AdminInfoService;
import com.afridi.gamingbackend.services.PlaystoreLinkService;
import com.afridi.gamingbackend.services.SignUpAndSignInService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class PlayStoreLinkController {


    private final PlaystoreLinkService playstoreLinkService;

    @PostMapping("/admin/create/playsore")
    public IdentityResponse createPlayStore(@RequestBody PlayStoreLInkRequest playStoreLInkRequest) {
       return playstoreLinkService.createPlayStoreLink(playStoreLInkRequest);

    }

    @GetMapping("/user/get/playsore")
    public List<PlayStoreLInkRequest> getPlayStore() {
        return playstoreLinkService.getStoreLink();

    }


}
