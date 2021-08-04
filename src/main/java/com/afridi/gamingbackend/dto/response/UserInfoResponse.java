package com.afridi.gamingbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoResponse {


    private String firstName;

    private String lastName;

    private String username;

    private String email;

    private String mobileNo;
    private Double acBalance;
    private int totalKill;
    private Double totalEarn;

    private Double winningBalance;


}
