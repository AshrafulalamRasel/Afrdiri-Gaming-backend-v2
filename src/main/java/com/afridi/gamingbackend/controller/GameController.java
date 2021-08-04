package com.afridi.gamingbackend.controller;


import com.afridi.gamingbackend.domain.model.GameEntity;
import com.afridi.gamingbackend.dto.request.GameActiveAndInActiveRequest;
import com.afridi.gamingbackend.dto.request.GameProfileUpdateRequest;
import com.afridi.gamingbackend.dto.request.GameSetRequest;
import com.afridi.gamingbackend.dto.request.RegistrationInGameRequest;
import com.afridi.gamingbackend.dto.response.RoomIdAndPasswordResponse;
import com.afridi.gamingbackend.dto.response.UserShowGamePlayer;
import com.afridi.gamingbackend.services.GameServices;
import com.afridi.gamingbackend.services.RoomIdService;
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
public class GameController {

    private GameServices gameServices;
    private UserGameInfoService userGameInfoService;
    private RoomIdService roomIdService;

    @PostMapping("/admin/create/game")
    public ResponseEntity<String> createGame(@RequestBody GameSetRequest gameSetRequest) {
        return gameServices.adminCreateGame(gameSetRequest);
    }

    @PutMapping("/admin/update/games/profile/{id}")
    public ResponseEntity<String> updateGameProfile(@PathVariable String id,@RequestBody GameProfileUpdateRequest gameProfileUpdateRequest){
        return gameServices.updateGameProfile(id,gameProfileUpdateRequest);
    }

    @GetMapping("/common/show/games/active")
    public List<GameEntity> getAllActiveGame() {
        return userGameInfoService.userShowAllActiveGame();
    }

    @GetMapping("/common/show/games")
    public List<GameEntity> getAllGame() {
        return userGameInfoService.userShowAllGame();
    }

    @GetMapping("/common/show/games/inactive")
    public List<GameEntity> getAllGameInActive() {
        return userGameInfoService.userShowAllGameInActive();
    }

    @PutMapping("/games/{gameId}/players/refund")
    public String getRefund(@PathVariable String gameId) {
        return userGameInfoService.getRefund(gameId);
    }
    @PutMapping("/games/{gameId}/players/{id}/refund")
    public String getRefund(@PathVariable String gameId,@PathVariable String id) {
        return userGameInfoService.getRefundByUserId(gameId,id);
    }
    @PutMapping("/registration/user/games/{gameId}")
    public ResponseEntity<String> registration(@PathVariable String gameId, @RequestBody RegistrationInGameRequest registrationInGameRequest) {
       return gameServices.registration(gameId, registrationInGameRequest);
    }

    @PutMapping("/admin/change/staus/games/{gameId}")
    public ResponseEntity<String> adminInActiveGameByGameId(@PathVariable String gameId, @RequestBody GameActiveAndInActiveRequest gameActiveAndInActiveRequest) {
       return gameServices.gameStatusActiveOrInActive(gameId, gameActiveAndInActiveRequest);
    }

    @GetMapping("/common/show/all/games")
    public List<GameEntity> getAllGames() {
        return userGameInfoService.userShowAllGames();
    }

    @GetMapping("/user/show/all/games/room/password/{gameId}")
    public ResponseEntity<RoomIdAndPasswordResponse> getRoomIdAndPassword(@PathVariable String gameId) {
        return roomIdService.getGameRegisterUserByGameId(gameId);
    }


    @GetMapping("/user/show/join/player/games/{gameId}")
    public ResponseEntity<UserShowGamePlayer> userShowJoinPlayer(@PathVariable String gameId) {
        return gameServices.userShowJoinPlayerInGame(gameId);
    }

}
