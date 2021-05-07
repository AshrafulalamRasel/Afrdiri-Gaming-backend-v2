package com.afridi.gamingbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserHistoryResponse {


    private String username;
    private String email;
    private String gameName;
    private String winningStatus;
    private int perKillInGame;
    private int gamePerInvest;
    private Double incomeInPerGame;
    private Double currentAccount;
    private Double reFound;

}
