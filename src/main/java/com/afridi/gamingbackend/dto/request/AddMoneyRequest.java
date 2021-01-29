package com.afridi.gamingbackend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AddMoneyRequest {

    private String paymentGetawayName;

    private Double amount;

    private int lastThreeDigitOfPayableMobileNo;


}
