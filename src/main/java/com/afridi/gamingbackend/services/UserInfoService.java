package com.afridi.gamingbackend.services;


import com.afridi.gamingbackend.config.LoggedUserInfo;
import com.afridi.gamingbackend.domain.model.GameEntity;
import com.afridi.gamingbackend.domain.model.PlayersProfileEntity;
import com.afridi.gamingbackend.domain.model.RegisterUsersInGameEntity;
import com.afridi.gamingbackend.domain.repository.GameRepository;
import com.afridi.gamingbackend.domain.repository.RegistrationUsersInGameRepository;
import com.afridi.gamingbackend.domain.repository.UserProfileRepository;
import com.afridi.gamingbackend.domain.repository.UserRepository;
import com.afridi.gamingbackend.dto.request.UserInfoRequest;
import com.afridi.gamingbackend.dto.response.UserInfoResponse;
import com.afridi.gamingbackend.exceptionHandalling.ResourceNotFoundException;
import com.afridi.gamingbackend.security.jwt.JwtProvider;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UserInfoService {

    @Autowired
    PasswordEncoder encoder;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SignUpAndSignInService signUpAndSignInService;

    @Autowired
    private UserProfileRepository userProfileRepository;
    @Autowired
    private RegistrationUsersInGameRepository registrationUsersInGameRepository;
    private final GameRepository gameRepository;

    public ResponseEntity<String> createUserInformation(UserInfoRequest userInfoRequest) {

        String uuid = signUpAndSignInService.getLoggedAuthUser();

        Optional<PlayersProfileEntity> userProfileClassOptional = userProfileRepository.findAllById(uuid);
        if (!userProfileClassOptional.isPresent()) {
            PlayersProfileEntity playersProfileEntity = new PlayersProfileEntity();

            playersProfileEntity.setId(uuid);
            playersProfileEntity.setFirstName(userInfoRequest.getFirstName());
            playersProfileEntity.setLastName(userInfoRequest.getLastName());
            playersProfileEntity.setMobileNo(userInfoRequest.getMobileNo());
            playersProfileEntity.setAcBalance(0.0);
            playersProfileEntity.setIsActive(true);
            userProfileRepository.saveAndFlush(playersProfileEntity);
        } else if (userProfileClassOptional.isPresent()) {
            throw new RuntimeException("Already Present: ");

        }


        return new ResponseEntity<>(uuid, HttpStatus.CREATED);
    }

    public ResponseEntity<UserInfoResponse> getUserInformation() {

        Double winningAmount = 0.0;
        int totalKill = 0;
        String uuid = signUpAndSignInService.getLoggedAuthUser();
//        List<GameEntity> allByGameOwnerId = gameRepository.findAll();
        Optional<PlayersProfileEntity> optionalUserProfileClass = userProfileRepository.findById(uuid);
        Optional<List<RegisterUsersInGameEntity>> optionalRegisterUsersInGameEntity =
                registrationUsersInGameRepository.findAllByUserId(uuid);

        if (!optionalUserProfileClass.isPresent()) {
            throw new ResourceNotFoundException("User Not Present");
        }
       if(optionalRegisterUsersInGameEntity.isPresent())
        {

            for(RegisterUsersInGameEntity registerUsersInGameEntity: optionalRegisterUsersInGameEntity.get())
            {
                winningAmount = winningAmount + registerUsersInGameEntity.getTotalEarn();
                totalKill = totalKill + registerUsersInGameEntity.getTotalKill();
//            for(RegisterUsersInGameEntity registerUsersInGameEntity: gameEntity.getRegisterUsersInGameEntities()) {
//                if(registerUsersInGameEntity.getUserId().equals(uuid)) {
//
//                }
//            }
            }
        }

        PlayersProfileEntity playersProfileEntity = optionalUserProfileClass.get();

        UserInfoResponse userInfoResponse = new UserInfoResponse();


        if(!optionalRegisterUsersInGameEntity.isPresent()) {

            userInfoResponse.setFirstName(playersProfileEntity.getFirstName() + " " + playersProfileEntity.getLastName());
            userInfoResponse.setAcBalance(playersProfileEntity.getAcBalance());
            userInfoResponse.setMobileNo(playersProfileEntity.getMobileNo());
            userInfoResponse.setTotalKill(0);
            userInfoResponse.setTotalEarn(0.0);
        }

        if(optionalRegisterUsersInGameEntity.isPresent()) {

           // RegisterUsersInGameEntity registerUsersInGameEntity = optionalRegisterUsersInGameEntity.get();
            userInfoResponse.setFirstName(playersProfileEntity.getFirstName() + " " + playersProfileEntity.getLastName());
            userInfoResponse.setAcBalance(playersProfileEntity.getAcBalance());
            userInfoResponse.setMobileNo(playersProfileEntity.getMobileNo());
            userInfoResponse.setTotalKill(totalKill);
            userInfoResponse.setTotalEarn(winningAmount);
        }


        LoggedUserInfo.setLoggedUserId(optionalUserProfileClass.get().getId());
        LoggedUserInfo.setFirstName(optionalUserProfileClass.get().getFirstName());
        LoggedUserInfo.setLastName(optionalUserProfileClass.get().getLastName());
        LoggedUserInfo.setAcBalance(optionalUserProfileClass.get().getAcBalance());
        LoggedUserInfo.setMobileNo(optionalUserProfileClass.get().getMobileNo());
        LoggedUserInfo.setIsActive(optionalUserProfileClass.get().getIsActive());

        return new ResponseEntity(userInfoResponse, HttpStatus.OK);
    }


}
