package com.afridi.gamingbackend.services;


import com.afridi.gamingbackend.domain.model.GameEntity;
import com.afridi.gamingbackend.domain.model.PlayersProfileEntity;
import com.afridi.gamingbackend.domain.model.RegisterUsersInGameEntity;
import com.afridi.gamingbackend.domain.repository.*;
import com.afridi.gamingbackend.dto.request.*;
import com.afridi.gamingbackend.dto.response.UserShowGamePlayer;
import com.afridi.gamingbackend.exceptionHandalling.ResourceNotFoundException;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;
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

                resumeAmountForGameJoin(registrationInGameRequest, loggedUserId, playersProfileEntity, gameEntity,1);
            }

            else if (registrationInGameRequest.getPartnerType().toLowerCase().equals("duo")){
                resumeAmountForGameJoin(registrationInGameRequest, loggedUserId, playersProfileEntity, gameEntity,2);
            }
            else if (registrationInGameRequest.getPartnerType().toLowerCase().equals("squad")) {
                resumeAmountForGameJoin(registrationInGameRequest, loggedUserId, playersProfileEntity, gameEntity,4);
            }

            else if (registrationInGameRequest.getPartnerType().toLowerCase().equals("squad vs squad")) {
                resumeAmountForGameJoin(registrationInGameRequest, loggedUserId, playersProfileEntity, gameEntity,4);
            }
            else {
                throw new RuntimeException("Insufficient Balance");
            }

        }

        else {
            throw new ResourceNotFoundException("Already Register In Game");
        }
        return new ResponseEntity("Successfully done", HttpStatus.OK);
    }

    private void resumeAmountForGameJoin(RegistrationInGameRequest registrationInGameRequest,
                                         String loggedUserId, PlayersProfileEntity playersProfileEntity,
                                         GameEntity gameEntity, int numberOfPlayer) {


        if (gameEntity.getEntryFee() == 0 && playersProfileEntity.getWinningBalance() > 0){

            RegisterUsersInGameEntity registerUsersInGameEntity = new RegisterUsersInGameEntity();


            registerUsersInGameEntity.setPlayerDetails_id(getIdentificationLogid());
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
            addBalanceRequest.setAmount(Double.valueOf(gameEntity.getEntryFee()*numberOfPlayer));
            userGameInfoService.resumeWiningBalance(loggedUserId, addBalanceRequest);

        }
        else if (playersProfileEntity.getWinningBalance() >= gameEntity.getEntryFee()*numberOfPlayer) {

            RegisterUsersInGameEntity registerUsersInGameEntity = new RegisterUsersInGameEntity();
            registerUsersInGameEntity.setPlayerDetails_id(getIdentificationLogid());
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
            addBalanceRequest.setAmount(Double.valueOf(gameEntity.getEntryFee()*numberOfPlayer));
            userGameInfoService.resumeWiningBalance(loggedUserId, addBalanceRequest);
        }


        else if (playersProfileEntity.getWinningBalance() < (gameEntity.getEntryFee()*numberOfPlayer)) {

            if (playersProfileEntity.getAcBalance() >= gameEntity.getEntryFee()*numberOfPlayer) {

                RegisterUsersInGameEntity registerUsersInGameEntity = new RegisterUsersInGameEntity();
                registerUsersInGameEntity.setPlayerDetails_id(getIdentificationLogid());
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
                addBalanceRequest.setAmount(Double.valueOf(gameEntity.getEntryFee()*numberOfPlayer));
                userGameInfoService.resumeBalance(loggedUserId, addBalanceRequest);
            }

            else {
                throw new RuntimeException("Insufficient Balance");
            }

        }



        else {
            throw new RuntimeException("Insufficient Balance");
        }


       /* if (playersProfileEntity.getWinningBalance() < (gameEntity.getEntryFee()*numberOfPlayer)){

            if (playersProfileEntity.getAcBalance() >= gameEntity.getEntryFee()*numberOfPlayer) {
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
                addBalanceRequest.setAmount(Double.valueOf(gameEntity.getEntryFee()*numberOfPlayer));
                userGameInfoService.resumeBalance(loggedUserId, addBalanceRequest);
            }

            else {
                throw new RuntimeException("Insufficient Balance");
            }
        }*/

      /*  else
        {
            if (playersProfileEntity.getWinningBalance() >= gameEntity.getEntryFee()*numberOfPlayer) {
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
                addBalanceRequest.setAmount(Double.valueOf(gameEntity.getEntryFee()*numberOfPlayer));
                userGameInfoService.resumeWiningBalance(loggedUserId, addBalanceRequest);
            }

            else {
                throw new RuntimeException("Insufficient Balance");
            }*/

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


    public ResponseEntity<UserShowGamePlayer> userShowJoinPlayerInGame(String gameId){

        Optional<GameEntity> gameEntityOptional = gameRepository.findAllById(gameId);
        GameEntity gameEntity = gameEntityOptional.get();

        int maxPlayer = 0;
        long play = registrationUsersInGameRepository.countAllByGameIdStatus(gameId);

        if (gameEntity.getGameType().toString().equals("SOLO")){
            maxPlayer = (int) (play*1);
        }
        else if (gameEntity.getGameType().toString().equals("DUO")){
            maxPlayer = (int) (play*2);
        }
       else if (gameEntity.getGameType().toString().equals("SQUARD")){
            maxPlayer = (int) (play*4);
        }
        else {
            maxPlayer = (int) (play*4);
        }



        System.out.println("play"+play);



        UserShowGamePlayer userShowGamePlayer = new UserShowGamePlayer();
        userShowGamePlayer.setTotalPlayerInGame(Integer.valueOf(gameEntity.getMaxPlayers()));
        userShowGamePlayer.setTotalJoinPlayerInGame(maxPlayer);

        return new ResponseEntity(userShowGamePlayer,HttpStatus.OK);



    }

    public Long getIdentificationLogid(){
        Random random = new Random();
        int randomWithNextInt = random.nextInt();
        return Long.valueOf(randomWithNextInt) ;
    }
}
