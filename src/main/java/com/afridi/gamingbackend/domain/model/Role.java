package com.afridi.gamingbackend.domain.model;

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
@Table(name = "JWT_ROLE")
public class Role {

    @Id
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(length = 60)
    private RoleName name;

}
