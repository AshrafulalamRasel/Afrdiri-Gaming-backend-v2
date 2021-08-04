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
@Table(name = "ADMIN_PROFILE")
public class AdminEntity extends BaseEntity {

    private String firstName;

    private String lastName;

    private String mobileNo;


    @Column(name = "ISACTIVE", nullable = false)
    private Boolean isActive;
}
