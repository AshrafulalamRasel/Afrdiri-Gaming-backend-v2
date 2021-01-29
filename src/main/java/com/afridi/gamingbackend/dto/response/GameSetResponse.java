package com.afridi.gamingbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GameSetResponse {

    private String gameNumber;

    private String gameType;


    private String gameName;


    private String version;

    private String map;

    private String gameStatus;

    private int totalPrize;

    private int perKillPrize;

    private int entryFee;

    private boolean gameIsActive;


}
