package com.afridi.gamingbackend.controller;


import com.afridi.gamingbackend.domain.model.MoneyRequestEntity;
import com.afridi.gamingbackend.dto.request.*;
import com.afridi.gamingbackend.dto.response.WithDrawMoneyResponse;
import com.afridi.gamingbackend.services.UserGameInfoService;
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
public class BalanceController {

    private UserGameInfoService userGameInfoService;

    @PutMapping("/admin/users/{userId}/balance/add")
    public ResponseEntity<String> addBalance(@PathVariable String userId, @RequestBody AddBalanceRequest addBalanceRequest) {
        return userGameInfoService.addBalance(userId, addBalanceRequest);
    }

    @PutMapping("/admin/users/{userId}/balance/substation")
    public ResponseEntity<String> substation(@PathVariable String userId, @RequestBody AddBalanceRequest addBalanceRequest) {
        return userGameInfoService.resumeBalance(userId, addBalanceRequest);
    }

    @PostMapping("/user/balance/add/request")
    public ResponseEntity<String> addMoneyRequest(@RequestBody AddMoneyRequest addBalanceRequest) {
        return userGameInfoService.addMoneyRequest(addBalanceRequest);
    }

    @GetMapping("/admin/balance/request/show")
    public ResponseEntity<List<MoneyRequestEntity>> getMoneyRequests() {
        return userGameInfoService.getMoneyRequests();
    }

    @GetMapping("/admin/show/balance/request/active")
    public ResponseEntity<List<MoneyRequestEntity>> getMoneyRequestsActive() {
        return userGameInfoService.getMoneyRequestsActive();
    }

    @GetMapping("/user/show/balance/request/allTransaction")
    public ResponseEntity<List<MoneyRequestEntity>> getMoneyRequestsAllTransaction() {
        return userGameInfoService.getMoneyWalletRequest();
    }

    @GetMapping("/admin/show/balance/request/inactive")
    public ResponseEntity<List<MoneyRequestEntity>> getMoneyRequestsInactive() {
        return userGameInfoService.getAllInActiveBalanceRequest();
    }

    @PutMapping("/admin/update/balance/approve/users/{userId}/requests/{id}")
    public ResponseEntity<String> updateActiveAndInActive(@PathVariable String id, @PathVariable("userId") String userId,
                                                          @RequestBody UserLoadBalRequest userLoadBalRequest) {
        return userGameInfoService.updateBalanceStatus(userId, id, userLoadBalRequest);
    }

    @PutMapping("/admin/update/balance/deny/users/{userId}/requests/{id}")
    public ResponseEntity<String> updateBanlanceDeny(@PathVariable String id, @PathVariable("userId") String userId,
                                                          @RequestBody UserLoadBalRequest userLoadBalRequest) {
        return userGameInfoService.updateBalanceStatusDeny(userId, id, userLoadBalRequest);
    }

    @PutMapping("/admin/update/balance/users/{userId}/game/{gameId}")
    public ResponseEntity<String> updateTotalEarnPerKill(@PathVariable("userId") String userId, @PathVariable("gameId") String gameId,
                                                         @RequestBody PerKillOnGameRequest perKillOnGameRequest) {
        return userGameInfoService.updateTotalEarnPerKill(userId, gameId, perKillOnGameRequest);
    }

    @PutMapping("/admin/update/balance/withdraw/{id}")
    public ResponseEntity<String> updateWithdrawRequest(@PathVariable String id) {
        return userGameInfoService.updateWithDrawResponse(id);
    }

    @PostMapping("/user/balance/withdraw/requested")
    public ResponseEntity<String> withDrawMoneyRequest(@RequestBody WithDrawMoneyRequest withDrawMoneyRequest) {
        return userGameInfoService.WithDrawMoneyRequest(withDrawMoneyRequest);
    }

    @GetMapping("/admin/show/withdraw/request/inactive")
    public ResponseEntity<List<WithDrawMoneyResponse>> getAllWithDrawResponse() {
        return userGameInfoService.getAllWithDrawResponse();
    }


    @GetMapping("/admin/show/withdraw/request/active")
    public ResponseEntity<List<WithDrawMoneyResponse>> getAllWithDrawResponseActive() {
        return userGameInfoService.getAllWithDrawResponseActive();
    }

    @GetMapping("/get/withdraw/request/byUsers")
    public ResponseEntity<List<WithDrawMoneyResponse>> getAllWithDrawResponseByUsers() {
        return userGameInfoService.getMoneyMoneyWithdrawRequest();
    }

}
