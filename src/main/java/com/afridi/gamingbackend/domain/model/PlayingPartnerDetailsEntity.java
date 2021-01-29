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
@Table(name = "PlayingPartnerDetailsEntity")
public class PlayingPartnerDetailsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "playingPartnerrDetails_id")
    @JsonIgnore
    private Long playingPartnerrDetails_id;

    private String partnerType;

    private String partnerOneName;

    private String partnerTwoName;

    private String partnerThreeName;


}
