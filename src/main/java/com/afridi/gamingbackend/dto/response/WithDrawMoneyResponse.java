package com.afridi.gamingbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WithDrawMoneyResponse {

    private String id;
    private String paymentGetawayName;
    private Double amount;
    private String lastThreeDigitOfPayableMobileNo;
    private String userName;
    private Double currentBalance;
    private LocalDateTime updatedAt;


}
