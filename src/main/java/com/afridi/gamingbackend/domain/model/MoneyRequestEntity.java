package com.afridi.gamingbackend.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "MoneyRequestEntity")
public class MoneyRequestEntity extends BaseEntity {


    private String name;

    private String userId;

    private String paymentGetawayName;

    private Double amount;

    private int lastThreeDigitOfPayableMobileNo;

    private boolean isAuthorityProcessed;
    private String balanceStatus;


}
