package com.afridi.gamingbackend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationInGameRequest {


    private String partnerType;

    private String partnerOneName;

    private String partnerTwoName;

    private String partnerThreeName;
    private String partnerNameFour;



}
