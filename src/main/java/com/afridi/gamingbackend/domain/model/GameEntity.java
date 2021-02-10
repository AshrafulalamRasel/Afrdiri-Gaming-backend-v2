package com.afridi.gamingbackend.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "GameEntity")
public class GameEntity extends BaseEntity {

    private String gameNumber;


    private String gameType;


    private String gameName;

    private String maxPlayers;

    private String version;

    private String map;

    private String gameStatus;

    private String gameplayOption;

    private String gameplayStartTime;

    private String roomId;

    private String roomPassword;

    private int totalPrize;

    private int winnerPrize;

    private int secondPrize;

    private int thirdPrize;

    private int perKillPrize;

    private int entryFee;

    @Column(name = "ISACTIVE", nullable = false)
    private boolean gameIsActive;

    private String gameOwnerId;

    @OneToMany(targetEntity = RegisterUsersInGameEntity.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "f_key", referencedColumnName = "id")
    private List<RegisterUsersInGameEntity> registerUsersInGameEntities;


}
