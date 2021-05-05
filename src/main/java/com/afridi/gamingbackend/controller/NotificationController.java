package com.afridi.gamingbackend.controller;

import com.afridi.gamingbackend.dto.request.NotificationRequest;
import com.afridi.gamingbackend.dto.response.IdentityResponse;
import com.afridi.gamingbackend.dto.response.UserHistoryResponse;
import com.afridi.gamingbackend.services.NotificationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class NotificationController {

private final NotificationService notificationService;

    @PostMapping("/notification/send")
    public ResponseEntity<IdentityResponse> insertNotification(@RequestBody NotificationRequest notificationRequest){

        return new ResponseEntity(notificationService.createNotification(notificationRequest), HttpStatus.OK);
    }
    @GetMapping("/getNotification/history/delete")
    public List<NotificationRequest> getHistoryProfile() {
        return notificationService.getNotificationList();
    }


}
