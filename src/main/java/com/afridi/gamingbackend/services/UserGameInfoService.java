package com.afridi.gamingbackend.services;


import com.afridi.gamingbackend.domain.model.*;
import com.afridi.gamingbackend.domain.repository.*;
import com.afridi.gamingbackend.dto.request.*;
import com.afridi.gamingbackend.dto.response.MoneyRequestedUser;
import com.afridi.gamingbackend.dto.response.WithDrawMoneyResponse;
import com.afridi.gamingbackend.exceptionHandalling.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@AllArgsConstructor
@Service
public class UserGameInfoService {

    @Autowired
    private final SignUpAndSignInService signUpAndSignInService;
    private final UserProfileRepository userProfileRepository;

    private final MoneyRequestRepository moneyRequestRepository;
    private final WithdrawRequestRepository withdrawRequestRepository;

    private final GameRepository gameRepository;
    private final RegistrationUsersInGameRepository registrationUsersInGameRepository;


    public List<GameEntity> userShowAllActiveGame() {

        return gameRepository.findAllActiveGameNative();

    }

    public List<GameEntity> userShowAllGameInActive() {

        return gameRepository.findAllInActiveGameNative();

    }


    public List<GameEntity> userShowAllGames() {

        return gameRepository.findAll();

    }

    public ResponseEntity<String> addBalance(String userId, AddBalanceRequest addBalanceRequest) {
        Optional<PlayersProfileEntity> optionalUserProfileClass = userProfileRepository.findById(userId);
        if (!optionalUserProfileClass.isPresent()) {
            throw new ResourceNotFoundException("User Not Found By User Id");
        }
        PlayersProfileEntity playersProfileEntity = optionalUserProfileClass.get();
        playersProfileEntity.setAcBalance(playersProfileEntity.getAcBalance() + addBalanceRequest.getAmount());
        userProfileRepository.save(playersProfileEntity);

        return new ResponseEntity("Successfully done", HttpStatus.OK);
    }

    public ResponseEntity<String> addBalanceRefund(String userId, AddBalanceRequest addBalanceRequest) {
        Optional<PlayersProfileEntity> optionalUserProfileClass = userProfileRepository.findById(userId);
        if (!optionalUserProfileClass.isPresent()) {
            throw new ResourceNotFoundException("User Not Found By User Id");
        }
        PlayersProfileEntity playersProfileEntity = optionalUserProfileClass.get();
        playersProfileEntity.setAcBalance(playersProfileEntity.getAcBalance() + addBalanceRequest.getAmount());
        playersProfileEntity.setReFound(addBalanceRequest.getAmount());
        userProfileRepository.save(playersProfileEntity);

        return new ResponseEntity("Successfully done", HttpStatus.OK);
    }

    public ResponseEntity<String> resumeBalance(String userId, AddBalanceRequest addBalanceRequest) {
        Optional<PlayersProfileEntity> optionalUserProfileClass = userProfileRepository.findById(userId);
        if (!optionalUserProfileClass.isPresent()) {
            throw new ResourceNotFoundException("User Not Found By User Id");
        }
        PlayersProfileEntity playersProfileEntity = optionalUserProfileClass.get();
        playersProfileEntity.setAcBalance(playersProfileEntity.getAcBalance() - addBalanceRequest.getAmount());
        userProfileRepository.save(playersProfileEntity);

        return new ResponseEntity("Successfully done", HttpStatus.OK);
    }

    public ResponseEntity<String> resumeWiningBalance(String userId, AddBalanceRequest addBalanceRequest) {
        Optional<PlayersProfileEntity> optionalUserProfileClass = userProfileRepository.findById(userId);
        if (!optionalUserProfileClass.isPresent()) {
            throw new ResourceNotFoundException("User Not Found By User Id");
        }
        PlayersProfileEntity playersProfileEntity = optionalUserProfileClass.get();

        double balanceAvailable = playersProfileEntity.getWinningBalance() - addBalanceRequest.getAmount();

        if (balanceAvailable > 0){

            playersProfileEntity.setWinningBalance(balanceAvailable);
            userProfileRepository.save(playersProfileEntity);

        }
        else {
            return new ResponseEntity("Successfully done", HttpStatus.FORBIDDEN);
        }



        return new ResponseEntity("Successfully done", HttpStatus.OK);
    }

    public ResponseEntity<String> addMoneyRequest(AddMoneyRequest addBalanceRequest) {

        UUID id = UUID.randomUUID();
        String uuid = id.toString();

        String loggedUserId = signUpAndSignInService.getLoggedAuthUser();
        Optional<PlayersProfileEntity> userProfileClassOptional = userProfileRepository.findAllById(loggedUserId);

        if (!userProfileClassOptional.isPresent()) {

            throw new RuntimeException("Not Here ...");
        }
        PlayersProfileEntity playersProfileEntity = userProfileClassOptional.get();

        MoneyRequestEntity moneyRequestEntity = new MoneyRequestEntity();


        moneyRequestEntity.setId(uuid);
        moneyRequestEntity.setUserId(loggedUserId);
        moneyRequestEntity.setName(playersProfileEntity.getFirstName() + "" + playersProfileEntity.getLastName());
        moneyRequestEntity.setAmount(addBalanceRequest.getAmount());
        moneyRequestEntity.setAuthorityProcessed(false);
        moneyRequestEntity.setPaymentGetawayName(addBalanceRequest.getPaymentGetawayName());
        moneyRequestEntity.setLastThreeDigitOfPayableMobileNo(addBalanceRequest.getLastThreeDigitOfPayableMobileNo());

        moneyRequestRepository.save(moneyRequestEntity);

        return new ResponseEntity<>(uuid, HttpStatus.CREATED);
    }

    public ResponseEntity<List<MoneyRequestEntity>> getMoneyRequests() {

        return new ResponseEntity(moneyRequestRepository.findAll(), HttpStatus.OK);
    }

    public ResponseEntity<List<MoneyRequestEntity>> getMoneyRequestsActive() {

        return new ResponseEntity(moneyRequestRepository.findAllActiveBalanceRequest(), HttpStatus.OK);
    }

    public ResponseEntity<List<MoneyRequestEntity>> getAllInActiveBalanceRequest() {

        return new ResponseEntity(moneyRequestRepository.findAllInActiveBalanceRequest(), HttpStatus.OK);
    }

    public ResponseEntity<String> updateBalanceStatus(String userId, String balanceId, UserLoadBalRequest userLoadBalRequest) {

        Optional<MoneyRequestEntity> optionalMoneyRequestEntity = moneyRequestRepository.findById(balanceId);
        if (!optionalMoneyRequestEntity.isPresent()) {
            throw new ResourceNotFoundException("User Not Found By User Id");
        }
        MoneyRequestEntity moneyRequestEntity = optionalMoneyRequestEntity.get();
        moneyRequestEntity.setAuthorityProcessed(true);
        moneyRequestEntity.setBalanceStatus("Success");
        moneyRequestRepository.save(moneyRequestEntity);

        AddBalanceRequest addBalanceRequest = new AddBalanceRequest();
        addBalanceRequest.setAmount(userLoadBalRequest.getBalance());
        addBalance(userId, addBalanceRequest);

        return new ResponseEntity("Successfully done", HttpStatus.OK);
    }

    public ResponseEntity<String> updateBalanceStatusDeny(String balanceId) {

        Optional<MoneyRequestEntity> optionalMoneyRequestEntity = moneyRequestRepository.findById(balanceId);
        if (!optionalMoneyRequestEntity.isPresent()) {
            throw new ResourceNotFoundException("User Not Found By User Id");
        }
        MoneyRequestEntity moneyRequestEntity = optionalMoneyRequestEntity.get();
        moneyRequestEntity.setAuthorityProcessed(false);
        moneyRequestEntity.setBalanceStatus("cancel");
        moneyRequestRepository.save(moneyRequestEntity);

        return new ResponseEntity("Successfully done", HttpStatus.OK);
    }


    public ResponseEntity<String> updateTotalEarnPerKill(String userId, String gameId, PerKillOnGameRequest perKillOnGameRequest) {

        Optional<PlayersProfileEntity> optionalUserProfileClass = userProfileRepository.findById(userId);

        Optional<GameEntity> gameEntityOptionalList = gameRepository.findById(gameId);
        Optional<RegisterUsersInGameEntity> registerUsersInGameEntity = registrationUsersInGameRepository.findAllByUserIdAndPlayerId(gameId, userId);

        if (!optionalUserProfileClass.isPresent()) {
            throw new ResourceNotFoundException("User Not Found By User Id");
        }
        if (!gameEntityOptionalList.isPresent()) {
            throw new ResourceNotFoundException("User Dose not Registration for Game");
        }


        GameEntity gameEntity = gameEntityOptionalList.get();
        PlayersProfileEntity playersProfileEntity = optionalUserProfileClass.get();
        RegisterUsersInGameEntity registerUsersInGameEntityList = registerUsersInGameEntity.get();

        if (gameEntity.getId().equals(gameId)) {

            if (perKillOnGameRequest.getPrize().toLowerCase().equals("winner")) {
                Double calculatePerKillAmount = (Double.valueOf(playersProfileEntity.getWinningBalance()) + (gameEntity.getWinnerPrize()) + ((gameEntity.getPerKillPrize()) * (perKillOnGameRequest.getPerKill())));
                playersProfileEntity.setWinningBalance(Double.valueOf(calculatePerKillAmount));

                registerUsersInGameEntityList.setTotalKill(registerUsersInGameEntityList.getTotalKill() + perKillOnGameRequest.getPerKill());
                registerUsersInGameEntityList.setTotalEarn(Double.valueOf(gameEntity.getWinnerPrize() + ((gameEntity.getPerKillPrize()) * (perKillOnGameRequest.getPerKill()))));
                registerUsersInGameEntityList.setGameWinningStatus("winner");
                registerUsersInGameEntityList.setStatusInGame(gameEntity.getWinnerPrize());
                registrationUsersInGameRepository.save(registerUsersInGameEntityList);
                userProfileRepository.save(playersProfileEntity);

            } else if (perKillOnGameRequest.getPrize().toLowerCase().replaceAll(" ", "").equals("runnerup")) {
                Double calculatePerKillAmount = (Double.valueOf(playersProfileEntity.getWinningBalance()) + (gameEntity.getSecondPrize()) + ((gameEntity.getPerKillPrize()) * (perKillOnGameRequest.getPerKill())));
                playersProfileEntity.setWinningBalance(Double.valueOf(calculatePerKillAmount));

                registerUsersInGameEntityList.setTotalKill(registerUsersInGameEntityList.getTotalKill() + perKillOnGameRequest.getPerKill());
                registerUsersInGameEntityList.setTotalEarn(Double.valueOf(gameEntity.getSecondPrize() + ((gameEntity.getPerKillPrize()) * (perKillOnGameRequest.getPerKill()))));
                registerUsersInGameEntityList.setStatusInGame(gameEntity.getSecondPrize());
                registerUsersInGameEntityList.setGameWinningStatus("runnerup");
                registrationUsersInGameRepository.save(registerUsersInGameEntityList);
                userProfileRepository.save(playersProfileEntity);

            } else if (perKillOnGameRequest.getPrize().toLowerCase().equals("third")) {

                Double calculatePerKillAmount = (Double.valueOf(playersProfileEntity.getWinningBalance()) + (gameEntity.getThirdPrize()) + ((gameEntity.getPerKillPrize()) * (perKillOnGameRequest.getPerKill())));
                playersProfileEntity.setWinningBalance(Double.valueOf(calculatePerKillAmount));

                registerUsersInGameEntityList.setTotalKill(registerUsersInGameEntityList.getTotalKill() + perKillOnGameRequest.getPerKill());
                registerUsersInGameEntityList.setTotalEarn(Double.valueOf(gameEntity.getThirdPrize() + ((gameEntity.getPerKillPrize()) * (perKillOnGameRequest.getPerKill()))));
                registerUsersInGameEntityList.setStatusInGame(gameEntity.getThirdPrize());
                registerUsersInGameEntityList.setGameWinningStatus("third");
                registrationUsersInGameRepository.save(registerUsersInGameEntityList);
                userProfileRepository.save(playersProfileEntity);

            } else if (perKillOnGameRequest.getPrize().toLowerCase().equals("none")) {

                Double calculatePerKillAmount = (Double.valueOf(playersProfileEntity.getWinningBalance()) + ((gameEntity.getPerKillPrize()) * (perKillOnGameRequest.getPerKill())));
                playersProfileEntity.setWinningBalance(calculatePerKillAmount);

                registerUsersInGameEntityList.setTotalKill(registerUsersInGameEntityList.getTotalKill() + perKillOnGameRequest.getPerKill());
                registerUsersInGameEntityList.setTotalEarn(Double.valueOf(((gameEntity.getPerKillPrize()) * (perKillOnGameRequest.getPerKill()))));
                registrationUsersInGameRepository.save(registerUsersInGameEntityList);
                userProfileRepository.save(playersProfileEntity);

            }


        }


        return new ResponseEntity("Successfully done", HttpStatus.OK);
    }

    public List<GameEntity> userShowAllGame() {
        return gameRepository.findAll();
    }


    public ResponseEntity<List<MoneyRequestEntity>> getMoneyWalletRequest() {

        String loggedUserId = signUpAndSignInService.getLoggedAuthUser();

        return new ResponseEntity(moneyRequestRepository.findAllByUserId(loggedUserId), HttpStatus.OK);
    }


    public ResponseEntity<String> WithDrawMoneyRequest(WithDrawMoneyRequest withDrawMoneyRequest) {

        UUID id = UUID.randomUUID();
        String uuid = id.toString();

        String loggedUserId = signUpAndSignInService.getLoggedAuthUser();
        Optional<PlayersProfileEntity> userProfileClassOptional = userProfileRepository.findAllById(loggedUserId);

        if (!userProfileClassOptional.isPresent()) {

            return new ResponseEntity<>("Not Present", HttpStatus.BAD_REQUEST);
        }
        PlayersProfileEntity playersProfileEntity = userProfileClassOptional.get();


        List<MoneyWithdrawRequestEntity> moneyWithdrawRequestEntityList = withdrawRequestRepository.findAllByUserId(loggedUserId);


        if (playersProfileEntity.getWinningBalance() >= withDrawMoneyRequest.getAmount()) {

            double remainWinBalance = userProfileClassOptional.get().getWinningBalance() - withDrawMoneyRequest.getAmount();

            if (remainWinBalance > 99){

                if (moneyWithdrawRequestEntityList.isEmpty()) {

                    MoneyWithdrawRequestEntity moneyWithdrawRequestEntity = new MoneyWithdrawRequestEntity();
                    moneyWithdrawRequestEntity.setId(uuid);
                    moneyWithdrawRequestEntity.setUserId(loggedUserId);
                    moneyWithdrawRequestEntity.setName(playersProfileEntity.getFirstName() + "" + playersProfileEntity.getLastName());
                    moneyWithdrawRequestEntity.setAmount(withDrawMoneyRequest.getAmount());
                    moneyWithdrawRequestEntity.setAuthorityProcessed(false);
                    moneyWithdrawRequestEntity.setPaymentGetawayName(withDrawMoneyRequest.getPaymentGetawayName());
                    moneyWithdrawRequestEntity.setLastThreeDigitOfPayableMobileNo(withDrawMoneyRequest.getLastThreeDigitOfPayableMobileNo());
                    moneyWithdrawRequestEntity.setCurrentBalance(remainWinBalance);
                    moneyWithdrawRequestEntity.setFlag("notapproave");

                    AddBalanceRequest addBalanceRequest = new AddBalanceRequest();
                    addBalanceRequest.setAmount(Double.valueOf(moneyWithdrawRequestEntity.getAmount()));
                    resumeWiningBalance(moneyWithdrawRequestEntity.getUserId(), addBalanceRequest);

                    withdrawRequestRepository.save(moneyWithdrawRequestEntity);

                }

                else if (!moneyWithdrawRequestEntityList.isEmpty()) {

                    for (MoneyWithdrawRequestEntity moneyWithdrawRequestList : moneyWithdrawRequestEntityList) {

                        if (moneyWithdrawRequestList.getFlag().toString().equals("notapproave")) {

                            return new ResponseEntity<>("Your Previous Money With Draw Pending", HttpStatus.BAD_REQUEST);
                        }

                        else {

                            MoneyWithdrawRequestEntity moneyWithdrawRequestEntity = new MoneyWithdrawRequestEntity();
                            moneyWithdrawRequestEntity.setId(uuid);
                            moneyWithdrawRequestEntity.setUserId(loggedUserId);
                            moneyWithdrawRequestEntity.setName(playersProfileEntity.getFirstName() + "" + playersProfileEntity.getLastName());
                            moneyWithdrawRequestEntity.setAmount(withDrawMoneyRequest.getAmount());
                            moneyWithdrawRequestEntity.setAuthorityProcessed(false);
                            moneyWithdrawRequestEntity.setPaymentGetawayName(withDrawMoneyRequest.getPaymentGetawayName());
                            moneyWithdrawRequestEntity.setLastThreeDigitOfPayableMobileNo(withDrawMoneyRequest.getLastThreeDigitOfPayableMobileNo());
                            moneyWithdrawRequestEntity.setCurrentBalance(userProfileClassOptional.get().getAcBalance());
                            moneyWithdrawRequestEntity.setFlag("NotApproval");

                            AddBalanceRequest addBalanceRequest = new AddBalanceRequest();
                            addBalanceRequest.setAmount(Double.valueOf(moneyWithdrawRequestEntity.getAmount()));
                            resumeWiningBalance(moneyWithdrawRequestEntity.getUserId(), addBalanceRequest);

                            withdrawRequestRepository.save(moneyWithdrawRequestEntity);

                        }
                    }
                }
            }

            else {

                return new ResponseEntity("Remainning Balance Less Then 100",HttpStatus.UNAUTHORIZED);
            }


        }else{
            return new ResponseEntity("Remainning Balance Less Then 100",HttpStatus.UNAUTHORIZED);
        }

        /*else if (playersProfileEntity.getWinningBalance() < withDrawMoneyRequest.getAmount()) {

            double remainAcBalance = userProfileClassOptional.get().getAcBalance() - withDrawMoneyRequest.getAmount();

            if (moneyWithdrawRequestEntityList.isEmpty()) {

                if (remainAcBalance >= 100) {

                    MoneyWithdrawRequestEntity moneyWithdrawRequestEntity = new MoneyWithdrawRequestEntity();
                    moneyWithdrawRequestEntity.setId(uuid);
                    moneyWithdrawRequestEntity.setUserId(loggedUserId);
                    moneyWithdrawRequestEntity.setName(playersProfileEntity.getFirstName() + "" + playersProfileEntity.getLastName());
                    moneyWithdrawRequestEntity.setAmount(withDrawMoneyRequest.getAmount());
                    moneyWithdrawRequestEntity.setAuthorityProcessed(false);
                    moneyWithdrawRequestEntity.setPaymentGetawayName(withDrawMoneyRequest.getPaymentGetawayName());
                    moneyWithdrawRequestEntity.setLastThreeDigitOfPayableMobileNo(withDrawMoneyRequest.getLastThreeDigitOfPayableMobileNo());
                    moneyWithdrawRequestEntity.setCurrentBalance(remainAcBalance);
                    moneyWithdrawRequestEntity.setFlag("notapproave");

                    AddBalanceRequest addBalanceRequest = new AddBalanceRequest();
                    addBalanceRequest.setAmount(Double.valueOf(moneyWithdrawRequestEntity.getAmount()));
                    resumeBalance(moneyWithdrawRequestEntity.getUserId(), addBalanceRequest);

                    withdrawRequestRepository.save(moneyWithdrawRequestEntity);

                }
            }
                else {

                    for (MoneyWithdrawRequestEntity moneyWithdrawRequestList: moneyWithdrawRequestEntityList) {

                        if (moneyWithdrawRequestList.getFlag().toString().equals("notapproave")) {

                            return new ResponseEntity<>("Your Previous Money With Draw Pending", HttpStatus.BAD_REQUEST);

                        }

                        else {
                            if (remainAcBalance >= 100) {

                                MoneyWithdrawRequestEntity moneyWithdrawRequestEntity = new MoneyWithdrawRequestEntity();
                                moneyWithdrawRequestEntity.setId(uuid);
                                moneyWithdrawRequestEntity.setUserId(loggedUserId);
                                moneyWithdrawRequestEntity.setName(playersProfileEntity.getFirstName() + "" + playersProfileEntity.getLastName());
                                moneyWithdrawRequestEntity.setAmount(withDrawMoneyRequest.getAmount());
                                moneyWithdrawRequestEntity.setAuthorityProcessed(false);
                                moneyWithdrawRequestEntity.setPaymentGetawayName(withDrawMoneyRequest.getPaymentGetawayName());
                                moneyWithdrawRequestEntity.setLastThreeDigitOfPayableMobileNo(withDrawMoneyRequest.getLastThreeDigitOfPayableMobileNo());
                                moneyWithdrawRequestEntity.setCurrentBalance(userProfileClassOptional.get().getAcBalance());
                                moneyWithdrawRequestEntity.setFlag("NotApproval");

                                AddBalanceRequest addBalanceRequest = new AddBalanceRequest();
                                addBalanceRequest.setAmount(Double.valueOf(moneyWithdrawRequestEntity.getAmount()));
                                resumeBalance(moneyWithdrawRequestEntity.getUserId(), addBalanceRequest);

                                withdrawRequestRepository.save(moneyWithdrawRequestEntity);
                                return new ResponseEntity<>(uuid, HttpStatus.CREATED);
                            }

                            else {
                                return new ResponseEntity<>("Withdraw Request Less than 100.", HttpStatus.BAD_REQUEST);

                            }


                        }
                    }
                }

        }*/

        return new ResponseEntity<>(uuid, HttpStatus.CREATED);
    }

    public ResponseEntity<String> updateWithDrawResponse(String id) {

        Optional<MoneyWithdrawRequestEntity> optionalMoneyWithdrawRequestEntity = withdrawRequestRepository.findById(id);
        MoneyWithdrawRequestEntity moneyWithdrawRequestEntity = optionalMoneyWithdrawRequestEntity.get();

        if (!optionalMoneyWithdrawRequestEntity.isPresent()) {

            throw new RuntimeException("User Not Found By User Id");
        }

        moneyWithdrawRequestEntity.setAuthorityProcessed(true);
        moneyWithdrawRequestEntity.setFlag("success");

        withdrawRequestRepository.save(moneyWithdrawRequestEntity);
        return new ResponseEntity<>("Okay", HttpStatus.OK);
    }

    public ResponseEntity<List<WithDrawMoneyResponse>> getAllWithDrawResponse() {

        List<MoneyWithdrawRequestEntity> moneyWithdrawRequestEntityList = withdrawRequestRepository.findAll();

        List<WithDrawMoneyResponse> withDrawMoneyRequestList = new ArrayList<>();

        for (MoneyWithdrawRequestEntity mmm : moneyWithdrawRequestEntityList) {

            if (mmm.isAuthorityProcessed() == false) {

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd uuuu hh:mm:ssa", Locale.ENGLISH);
                LocalDateTime localDateTime = mmm.getUpdatedAt();
                String formatDateTime = localDateTime.format(formatter);
                withDrawMoneyRequestList.add(new WithDrawMoneyResponse(mmm.getId(), mmm.getPaymentGetawayName(),
                        mmm.getAmount(), mmm.getLastThreeDigitOfPayableMobileNo(), mmm.getName(), mmm.getCurrentBalance(), formatDateTime, mmm.isAuthorityProcessed()));
            }

        }

        return new ResponseEntity(withDrawMoneyRequestList, HttpStatus.OK);
    }


    public ResponseEntity<List<WithDrawMoneyResponse>> getAllWithDrawResponseActive() {

        List<MoneyWithdrawRequestEntity> moneyWithdrawRequestEntityList = withdrawRequestRepository.findAll();

        List<WithDrawMoneyResponse> withDrawMoneyRequestList = new ArrayList<>();

        for (MoneyWithdrawRequestEntity mmm : moneyWithdrawRequestEntityList) {


            if (mmm.isAuthorityProcessed() == true) {

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd uuuu hh:mm:ssa", Locale.ENGLISH);
                LocalDateTime localDateTime = mmm.getUpdatedAt();
                String formatDateTime = localDateTime.format(formatter);

                withDrawMoneyRequestList.add(new WithDrawMoneyResponse(mmm.getId(), mmm.getPaymentGetawayName(),
                        mmm.getAmount(), mmm.getLastThreeDigitOfPayableMobileNo(), mmm.getName(), mmm.getCurrentBalance(), formatDateTime, mmm.isAuthorityProcessed()));
            }

        }

        return new ResponseEntity(withDrawMoneyRequestList, HttpStatus.OK);
    }


    public ResponseEntity<List<WithDrawMoneyResponse>> getMoneyMoneyWithdrawRequest() {

        String loggedUserId = signUpAndSignInService.getLoggedAuthUser();

        List<MoneyWithdrawRequestEntity> moneyWithdrawRequestEntityList = withdrawRequestRepository.findAllByUserId(loggedUserId);

        List<WithDrawMoneyResponse> withDrawMoneyRequestList = new ArrayList<>();

        for (MoneyWithdrawRequestEntity mmm : moneyWithdrawRequestEntityList) {


            if (mmm.isAuthorityProcessed() == true) {

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd uuuu hh:mm:ssa", Locale.ENGLISH);
                LocalDateTime localDateTime = mmm.getUpdatedAt();
                String formatDateTime = localDateTime.format(formatter);

                withDrawMoneyRequestList.add(new WithDrawMoneyResponse(mmm.getId(), mmm.getPaymentGetawayName(),
                        mmm.getAmount(), mmm.getLastThreeDigitOfPayableMobileNo(), mmm.getName(), mmm.getCurrentBalance(), formatDateTime, mmm.isAuthorityProcessed()));
            }

        }


        return new ResponseEntity(withDrawMoneyRequestList, HttpStatus.OK);
    }

    public String getRefund(String gameId) {


        Optional<GameEntity> gameEntity = gameRepository.findAllById(gameId);

        if (!gameEntity.isPresent()) {
            throw new ResourceNotFoundException("Game Not Found");
        }
        GameEntity gameEntityManager = gameEntity.get();

        for (RegisterUsersInGameEntity registerUsersInGameEntity : gameEntity.get().getRegisterUsersInGameEntities()) {
            if (gameEntityManager.getGameType().toLowerCase().equals("solo")) {

                int registerAmount = gameEntity.get().getEntryFee();
                AddBalanceRequest addBalanceRequest = new AddBalanceRequest();
                addBalanceRequest.setAmount(Double.valueOf(registerAmount));
                addBalance(registerUsersInGameEntity.getUserId(), addBalanceRequest);
            } else if (gameEntityManager.getGameType().toLowerCase().equals("duo")) {

                int registerAmount = gameEntity.get().getEntryFee();
                AddBalanceRequest addBalanceRequest = new AddBalanceRequest();
                addBalanceRequest.setAmount(Double.valueOf(registerAmount * 2));
                addBalance(registerUsersInGameEntity.getUserId(), addBalanceRequest);
            } else if (gameEntityManager.getGameType().toLowerCase().equals("squad")) {
                int registerAmount = gameEntity.get().getEntryFee();
                AddBalanceRequest addBalanceRequest = new AddBalanceRequest();
                addBalanceRequest.setAmount(Double.valueOf(registerAmount * 4));
                addBalance(registerUsersInGameEntity.getUserId(), addBalanceRequest);
            } else if (gameEntityManager.getGameType().toLowerCase().equals("squadvssquad")) {
                int registerAmount = gameEntity.get().getEntryFee();
                AddBalanceRequest addBalanceRequest = new AddBalanceRequest();
                addBalanceRequest.setAmount(Double.valueOf(registerAmount * 4));
                addBalance(registerUsersInGameEntity.getUserId(), addBalanceRequest);
            }

        }
        return "added";
    }

    public String getRefundByUserId(String gameId, String id) {
        Optional<GameEntity> gameEntity = gameRepository.findAllById(gameId);

        if (!gameEntity.isPresent()) {
            throw new ResourceNotFoundException("Game Not Found");
        }
        GameEntity gameEntityManager = gameEntity.get();

        if (gameEntityManager.getGameType().toLowerCase().equals("solo")) {

            int registerAmount = gameEntity.get().getEntryFee();
            AddBalanceRequest addBalanceRequest = new AddBalanceRequest();
            addBalanceRequest.setAmount(Double.valueOf(registerAmount));
            addBalanceRefund(id, addBalanceRequest);
        } else if (gameEntityManager.getGameType().toLowerCase().equals("duo")) {

            int registerAmount = gameEntity.get().getEntryFee();
            AddBalanceRequest addBalanceRequest = new AddBalanceRequest();
            addBalanceRequest.setAmount(Double.valueOf(registerAmount * 2));
            addBalanceRefund(id, addBalanceRequest);
        } else if (gameEntityManager.getGameType().toLowerCase().equals("squad")) {

            int registerAmount = gameEntity.get().getEntryFee();
            AddBalanceRequest addBalanceRequest = new AddBalanceRequest();
            addBalanceRequest.setAmount(Double.valueOf(registerAmount * 4));
            addBalanceRefund(id, addBalanceRequest);
        } else if (gameEntityManager.getGameType().toLowerCase().equals("squad vs squad")) {

            int registerAmount = gameEntity.get().getEntryFee();
            AddBalanceRequest addBalanceRequest = new AddBalanceRequest();
            addBalanceRequest.setAmount(Double.valueOf(registerAmount * 4));
            addBalanceRefund(id, addBalanceRequest);
        } else {
            throw new RuntimeException("Not found");
        }


        return "added";
    }

    public ResponseEntity<List<MoneyRequestedUser>> getAllMoneyRequestedActiveUser(){

        List<MoneyRequestEntity> moneyRequestEntityList = moneyRequestRepository.findAll();

        List<MoneyRequestedUser> moneyRequestedUserList = new ArrayList<>();

        for (MoneyRequestEntity moneyRequestEntity : moneyRequestEntityList){

            if (moneyRequestEntity.isAuthorityProcessed() == true){

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd uuuu hh:mm:ssa", Locale.ENGLISH);
                LocalDateTime localDateTime = moneyRequestEntity.getUpdatedAt();
                String formatDateTime = localDateTime.format(formatter);
                moneyRequestedUserList.add(new MoneyRequestedUser(moneyRequestEntity.getName(),
                                                                 moneyRequestEntity.getPaymentGetawayName(),
                                                                 moneyRequestEntity.getAmount(),
                                                                 moneyRequestEntity.getLastThreeDigitOfPayableMobileNo(),
                                                                 moneyRequestEntity.getBalanceStatus(),formatDateTime));

            }
        }

        return new ResponseEntity(moneyRequestedUserList,HttpStatus.OK);
    }
}
