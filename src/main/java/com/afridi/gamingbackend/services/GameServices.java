package com.afridi.gamingbackend.services;


import com.afridi.gamingbackend.domain.model.GameEntity;
import com.afridi.gamingbackend.domain.model.PlayersProfileEntity;
import com.afridi.gamingbackend.domain.model.RegisterUsersInGameEntity;
import com.afridi.gamingbackend.domain.repository.*;
import com.afridi.gamingbackend.dto.request.*;
import com.afridi.gamingbackend.exceptionHandalling.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class GameServices {

    private final GameRepository gameRepository;
    private final UserGameInfoService userGameInfoService;
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final SignUpAndSignInService signUpAndSignInService;
    private final PlayingPartnerDetailsRepository playingPartnerDetailsRepository;
    private final RegistrationUsersInGameRepository registrationUsersInGameRepository;

    public ResponseEntity<String> adminCreateGame(GameSetRequest gameSetRequest) {

        UUID id = UUID.randomUUID();
        String uuid = id.toString();
        String OwnerId = signUpAndSignInService.getLoggedAuthUser();
        GameEntity gameEntity = new GameEntity();

        gameEntity.setId(uuid);
        gameEntity.setGameNumber(gameSetRequest.getGameNumber());
        gameEntity.setGameName(gameSetRequest.getGameName());
        gameEntity.setMaxPlayers(gameSetRequest.getMaxPlayers());
        gameEntity.setGameType(gameSetRequest.getGameType());
        gameEntity.setGameStatus(gameSetRequest.getGameStatus());
        gameEntity.setVersion(gameSetRequest.getVersion());
        gameEntity.setMap(gameSetRequest.getMap());
        gameEntity.setRoomId(gameSetRequest.getRoomId());
        gameEntity.setRoomPassword(gameSetRequest.getRoomPassword());
        gameEntity.setGameStatus(gameSetRequest.getGameStatus());
        gameEntity.setTotalPrize(gameSetRequest.getTotalPrize());
        gameEntity.setPerKillPrize(gameSetRequest.getPerKillPrize());
        gameEntity.setEntryFee(gameSetRequest.getEntryFee());
        gameEntity.setGameOwnerId(OwnerId);
        gameEntity.setGameIsActive(true);
        gameEntity.setWinnerPrize(gameSetRequest.getWinnerPrize());
        gameEntity.setSecondPrize(gameSetRequest.getSecondPrize());
        gameEntity.setThirdPrize(gameSetRequest.getThirdPrize());
        gameEntity.setGameplayOption(gameSetRequest.getGameplayOption());
        gameEntity.setGameplayStartTime(gameSetRequest.getGameplayStartTime());
        gameRepository.saveAndFlush(gameEntity);
        return new ResponseEntity<>(uuid, HttpStatus.CREATED);
    }


    public ResponseEntity<String> updateGameProfile(String gameId, GameProfileUpdateRequest gameProfileUpdateRequest) {

        Optional<GameEntity> optionalMoneyRequestEntity = gameRepository.findById(gameId);

        if (!optionalMoneyRequestEntity.isPresent()) {
            throw new ResourceNotFoundException("User Not Found By User Id");
        }
        GameEntity gameEntity = optionalMoneyRequestEntity.get();
        gameEntity.setRoomId(gameProfileUpdateRequest.getRoomId());
        gameEntity.setRoomPassword(gameProfileUpdateRequest.getRoomPassword());

        gameRepository.save(gameEntity);

        return new ResponseEntity("Successfully done", HttpStatus.OK);
    }


    public ResponseEntity<String> registration(String gameId, RegistrationInGameRequest registrationInGameRequest) {

        boolean isAlreadyRegistered = false;

        String loggedUserId = signUpAndSignInService.getLoggedAuthUser();
        Optional<GameEntity> gameEntityOptional = gameRepository.findAllById(gameId);

        Optional<PlayersProfileEntity> optionalUserProfileClass = userProfileRepository.findAllById(loggedUserId);
        PlayersProfileEntity playersProfileEntity = optionalUserProfileClass.get();

        if (!optionalUserProfileClass.isPresent()) {
            throw new RuntimeException("Access Deny In User Panel");
        }

        GameEntity gameEntity = gameEntityOptional.get();
        for (RegisterUsersInGameEntity registerUsersInGameEntity : gameEntity.getRegisterUsersInGameEntities()) {
            if (registerUsersInGameEntity.getUserId().equals(loggedUserId)) {
                isAlreadyRegistered = true;
            }
        }

        if (!isAlreadyRegistered) {

            if (registrationInGameRequest.getPartnerType().toLowerCase().equals("solo")) {

                if (playersProfileEntity.getAcBalance() >= gameEntity.getEntryFee()) {
                    RegisterUsersInGameEntity registerUsersInGameEntity = new RegisterUsersInGameEntity();
                    registerUsersInGameEntity.setUserId(loggedUserId);
                    registerUsersInGameEntity.setTotalEarn(0.0);
                    registerUsersInGameEntity.setPartnerType(registrationInGameRequest.getPartnerType());
                    registerUsersInGameEntity.setPartnerOneName(registrationInGameRequest.getPartnerOneName());
                    registerUsersInGameEntity.setPartnerTwoName(registrationInGameRequest.getPartnerTwoName());
                    registerUsersInGameEntity.setPartnerThreeName(registrationInGameRequest.getPartnerThreeName());
                    registerUsersInGameEntity.setPartnerNameFour(registrationInGameRequest.getPartnerNameFour());
                    registerUsersInGameEntity.setGameIdStatus(gameEntity.getId());
                    registerUsersInGameEntity.setTotalKill(0);
                    gameEntity.getRegisterUsersInGameEntities().add(registerUsersInGameEntity);
                    gameRepository.save(gameEntity);
                    AddBalanceRequest addBalanceRequest = new AddBalanceRequest();
                    addBalanceRequest.setAmount(Double.valueOf(gameEntity.getEntryFee()));
                    userGameInfoService.resumeBalance(loggedUserId, addBalanceRequest);
                }
                else {
                    throw new RuntimeException("Insufficient Balance");
                }
            }

            else if (registrationInGameRequest.getPartnerType().toLowerCase().equals("duo")){

                if (playersProfileEntity.getAcBalance() >= gameEntity.getEntryFee()*2){

                    RegisterUsersInGameEntity registerUsersInGameEntity = new RegisterUsersInGameEntity();
                    registerUsersInGameEntity.setUserId(loggedUserId);
                    registerUsersInGameEntity.setTotalEarn(0.0);
                    registerUsersInGameEntity.setPartnerType(registrationInGameRequest.getPartnerType());
                    registerUsersInGameEntity.setPartnerOneName(registrationInGameRequest.getPartnerOneName());
                    registerUsersInGameEntity.setPartnerTwoName(registrationInGameRequest.getPartnerTwoName());
                    registerUsersInGameEntity.setPartnerThreeName(registrationInGameRequest.getPartnerThreeName());
                    registerUsersInGameEntity.setPartnerNameFour(registrationInGameRequest.getPartnerNameFour());
                    registerUsersInGameEntity.setGameIdStatus(gameEntity.getId());
                    registerUsersInGameEntity.setTotalKill(0);
                    gameEntity.getRegisterUsersInGameEntities().add(registerUsersInGameEntity);
                    gameRepository.save(gameEntity);
                    AddBalanceRequest addBalanceRequest = new AddBalanceRequest();
                    addBalanceRequest.setAmount(Double.valueOf(gameEntity.getEntryFee()) * 2);
                    userGameInfoService.resumeBalance(loggedUserId, addBalanceRequest);
                }
                else {
                    throw new RuntimeException("Insufficient Balance");
                }

            }
            else if (registrationInGameRequest.getPartnerType().toLowerCase().equals("squad")) {

                if (playersProfileEntity.getAcBalance() >= gameEntity.getEntryFee()*4){
                    RegisterUsersInGameEntity registerUsersInGameEntity = new RegisterUsersInGameEntity();
                    registerUsersInGameEntity.setUserId(loggedUserId);
                    registerUsersInGameEntity.setTotalEarn(0.0);
                    registerUsersInGameEntity.setPartnerType(registrationInGameRequest.getPartnerType());
                    registerUsersInGameEntity.setPartnerOneName(registrationInGameRequest.getPartnerOneName());
                    registerUsersInGameEntity.setPartnerTwoName(registrationInGameRequest.getPartnerTwoName());
                    registerUsersInGameEntity.setPartnerThreeName(registrationInGameRequest.getPartnerThreeName());
                    registerUsersInGameEntity.setPartnerNameFour(registrationInGameRequest.getPartnerNameFour());
                    registerUsersInGameEntity.setGameIdStatus(gameEntity.getId());
                    registerUsersInGameEntity.setTotalKill(0);
                    gameEntity.getRegisterUsersInGameEntities().add(registerUsersInGameEntity);
                    gameRepository.save(gameEntity);
                    AddBalanceRequest addBalanceRequest = new AddBalanceRequest();
                    addBalanceRequest.setAmount(Double.valueOf(gameEntity.getEntryFee()) * 4);
                    userGameInfoService.resumeBalance(loggedUserId, addBalanceRequest);
                }
                else {
                    throw new RuntimeException("Insufficient Balance");
                }
            }

            else if (registrationInGameRequest.getPartnerType().toLowerCase().equals("squad vs squad")) {

                if (playersProfileEntity.getAcBalance() >= gameEntity.getEntryFee()*4){
                    RegisterUsersInGameEntity registerUsersInGameEntity = new RegisterUsersInGameEntity();
                    registerUsersInGameEntity.setUserId(loggedUserId);
                    registerUsersInGameEntity.setTotalEarn(0.0);
                    registerUsersInGameEntity.setPartnerType(registrationInGameRequest.getPartnerType());
                    registerUsersInGameEntity.setPartnerOneName(registrationInGameRequest.getPartnerOneName());
                    registerUsersInGameEntity.setPartnerTwoName(registrationInGameRequest.getPartnerTwoName());
                    registerUsersInGameEntity.setPartnerThreeName(registrationInGameRequest.getPartnerThreeName());
                    registerUsersInGameEntity.setPartnerNameFour(registrationInGameRequest.getPartnerNameFour());
                    registerUsersInGameEntity.setGameIdStatus(gameEntity.getId());
                    registerUsersInGameEntity.setTotalKill(0);
                    gameEntity.getRegisterUsersInGameEntities().add(registerUsersInGameEntity);
                    gameRepository.save(gameEntity);
                    AddBalanceRequest addBalanceRequest = new AddBalanceRequest();
                    addBalanceRequest.setAmount(Double.valueOf(gameEntity.getEntryFee()) * 4);
                    userGameInfoService.resumeBalance(loggedUserId, addBalanceRequest);
                }
                else {
                    throw new RuntimeException("Insufficient Balance");
                }
            }
            else {
                throw new RuntimeException("Insufficient Balance");
            }

        }

        else {
            throw new RuntimeException("Already Register In Game");
        }
        return new ResponseEntity("Successfully done", HttpStatus.OK);
    }


    public ResponseEntity<String> gameStatusActiveOrInActive(String gameId, GameActiveAndInActiveRequest gameActiveAndInActiveRequest) {

        Optional<GameEntity> gameEntityOptional = gameRepository.findAllById(gameId);

        if (!gameEntityOptional.isPresent()) {
            throw new RuntimeException("Not Available Game Here!!");
        }

        GameEntity gameEntity = gameEntityOptional.get();
        gameEntity.setGameIsActive(gameActiveAndInActiveRequest.isGameIsActive());

        gameRepository.save(gameEntity);
        return new ResponseEntity("Successfully done", HttpStatus.OK);
    }

}
