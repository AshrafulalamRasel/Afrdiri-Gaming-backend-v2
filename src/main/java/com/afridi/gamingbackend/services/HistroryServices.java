package com.afridi.gamingbackend.services;


import com.afridi.gamingbackend.domain.model.GameEntity;
import com.afridi.gamingbackend.domain.model.PlayersEntity;
import com.afridi.gamingbackend.domain.model.PlayersProfileEntity;
import com.afridi.gamingbackend.domain.model.RegisterUsersInGameEntity;
import com.afridi.gamingbackend.domain.repository.*;
import com.afridi.gamingbackend.dto.request.*;
import com.afridi.gamingbackend.dto.response.GameHistoryQuery;
import com.afridi.gamingbackend.dto.response.UserHistoryResponse;
import com.afridi.gamingbackend.exceptionHandalling.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@AllArgsConstructor
@Service
public class HistroryServices {

    private final GameRepository gameRepository;
    private final UserGameInfoService userGameInfoService;
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final SignUpAndSignInService signUpAndSignInService;
    private final PlayingPartnerDetailsRepository playingPartnerDetailsRepository;
    private final RegistrationUsersInGameRepository registrationUsersInGameRepository;


    public List<UserHistoryResponse> getHistoryByUserLoggedId() {


        List<UserHistoryResponse> userHistoryResponseList = new ArrayList<>();


        String ownerId = signUpAndSignInService.getLoggedAuthUser();

        Optional<PlayersEntity> optionalPlayersEntity = userRepository.findAllById(ownerId);
        Optional<PlayersProfileEntity> playersProfileEntity = userProfileRepository.findAllById(ownerId);

        PlayersProfileEntity profileEntity = playersProfileEntity.get();
        UserHistoryResponse userHistoryResponse = new UserHistoryResponse();

        if (!optionalPlayersEntity.isPresent()) {
            throw new RuntimeException("Not Found User!!");
        }

        PlayersEntity playersEntity = optionalPlayersEntity.get();

        userHistoryResponse.setUsername(playersEntity.getUsername());
        userHistoryResponse.setEmail(playersEntity.getEmail());


        userHistoryResponse.setCurrentAccount(profileEntity.getAcBalance());
        userHistoryResponse.setReFound(profileEntity.getReFound());

        Optional<List<RegisterUsersInGameEntity>> registerUsersInGameEntityList = registrationUsersInGameRepository.findAllGameByUserId(ownerId);

        if (registerUsersInGameEntityList.isPresent()) {

            for (RegisterUsersInGameEntity registerUsersInGameEntity : registerUsersInGameEntityList.get()) {

                userHistoryResponse.setGameName(registerUsersInGameEntity.getPartnerType());
                userHistoryResponse.setPerKillInGame(registerUsersInGameEntity.getTotalKill());
                userHistoryResponse.setIncomeInPerGame(registerUsersInGameEntity.getTotalEarn());
                Optional<GameEntity> entityList = gameRepository.findAllById(registerUsersInGameEntity.getGameIdStatus());

                GameEntity gameEntity = entityList.get();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd uuuu hh:mm:ssa", Locale.ENGLISH);
                LocalDateTime localDateTime = gameEntity.getUpdatedAt();
                String formatDateTime = localDateTime.format(formatter);
                userHistoryResponse.setUpdatedAt(formatDateTime);

                if (registerUsersInGameEntity.getStatusInGame() == gameEntity.getWinnerPrize()) {
                    userHistoryResponse.setWinningStatus("Winner");
                }
                if (registerUsersInGameEntity.getStatusInGame() == gameEntity.getSecondPrize()) {
                    userHistoryResponse.setWinningStatus("First Runner Up");
                }
                if (registerUsersInGameEntity.getStatusInGame() == gameEntity.getThirdPrize()) {
                    userHistoryResponse.setWinningStatus("Second Runner Up ");
                }

                if (entityList.isPresent()) {

                    if (gameEntity.getGameType().toLowerCase().equals("solo")) {
                        userHistoryResponse.setGamePerInvest(gameEntity.getEntryFee());
                    } else if (gameEntity.getGameType().toLowerCase().equals("duo")) {
                        userHistoryResponse.setGamePerInvest(gameEntity.getEntryFee() * 2);
                    } else if (gameEntity.getGameType().toLowerCase().equals("squad")) {
                        userHistoryResponse.setGamePerInvest(gameEntity.getEntryFee() * 4);
                    } else if (gameEntity.getGameType().toLowerCase().equals("squad vs squad")) {
                        userHistoryResponse.setGamePerInvest(gameEntity.getEntryFee() * 4);
                    }
                }

            }
        }

        userHistoryResponseList.add(userHistoryResponse);
        return userHistoryResponseList;
    }


}
