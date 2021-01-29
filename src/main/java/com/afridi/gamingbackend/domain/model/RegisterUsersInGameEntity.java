package com.afridi.gamingbackend.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "RegisterUsersInGameEntity")
public class RegisterUsersInGameEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "playerDetails_id")
    @JsonIgnore
    private Long playerDetails_id;

    private String userId;

    private int totalKill;

    private Double totalEarn;

    private String partnerType;

    private String partnerOneName;

    private String partnerTwoName;

    private String partnerThreeName;
    private String partnerNameFour;

}
