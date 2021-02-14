package com.afridi.gamingbackend.services;

import com.afridi.gamingbackend.domain.model.GameEntity;
import com.afridi.gamingbackend.domain.model.RegisterUsersInGameEntity;
import com.afridi.gamingbackend.domain.repository.GameRepository;
import com.afridi.gamingbackend.domain.repository.RegistrationUsersInGameRepository;
import com.afridi.gamingbackend.dto.response.RoomIdAndPasswordResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class RoomIdService {

    private final RegistrationUsersInGameRepository registrationUsersInGameRepository;
    private final SignUpAndSignInService signUpAndSignInService;
    private final GameRepository gameRepository;

    public ResponseEntity<RoomIdAndPasswordResponse> getGameRegisterUserByGameId(String gameId){

        String loggedUserId = signUpAndSignInService.getLoggedAuthUser();
        Optional<RegisterUsersInGameEntity> gameIdInformation = registrationUsersInGameRepository.findAllUserBygameId(gameId);

        List<RoomIdAndPasswordResponse> roomIdAndPasswordResponses= new ArrayList<>();

        if (gameIdInformation.get().getUserId().equals(loggedUserId)){

            Optional<GameEntity> optionalGameRepository = gameRepository.findAllById(gameId);

            GameEntity gameEntity = optionalGameRepository.get();

            roomIdAndPasswordResponses.add(new RoomIdAndPasswordResponse(gameEntity.getRoomId(),gameEntity.getRoomPassword()));

            return new ResponseEntity(roomIdAndPasswordResponses, HttpStatus.OK);

        }

        else if (!gameIdInformation.get().getUserId().equals(loggedUserId)){

            return new ResponseEntity("Not Found User In Game Registration ", HttpStatus.OK);
        }



        return new ResponseEntity(roomIdAndPasswordResponses, HttpStatus.OK);
    }




}
