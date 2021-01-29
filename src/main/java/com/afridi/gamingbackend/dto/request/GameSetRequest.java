package com.afridi.gamingbackend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GameSetRequest {

    private String gameNumber;

    private String gameType;

    private String gameName;

    private String version;

    private String map;

    private String gameStatus;

    private String roomId;

    private String roomPassword;

    private int totalPrize;

    private int winnerPrize;

    private int secondPrize;

    private int thirdPrize;

    private int perKillPrize;

    private int entryFee;
    

}
