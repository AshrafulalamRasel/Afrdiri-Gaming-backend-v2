package com.afridi.gamingbackend.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "CREATE_PLAYER_PROFILE")
public class PlayersProfileEntity extends BaseEntity {


    private String firstName;


    private String lastName;

    private String mobileNo;

    private Double acBalance;
    private Double winningBalance;
    private Double reFound;

    @Column(name = "ISACTIVE", nullable = false)
    private Boolean isActive;
}
