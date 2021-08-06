package com.afridi.gamingbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MoneyRequestedUser {

    private String name;

    private String paymentGetawayName;

    private Double amount;

    private int lastThreeDigitOfPayableMobileNo;

    private String balanceStatus;
    private String updatedAt;

}
