package com.afridi.gamingbackend.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PlayerProfileInGameEntity")
public class PlayerProfileInGameEntity extends BaseEntity {


    private int totalKillPerGame;

    private int totalBalance;

    @ManyToOne
    private GameEntity gameEntity;

    @ManyToOne
    private PlayersProfileEntity playersProfileEntity;



}
