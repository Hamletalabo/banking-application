package com.hamlet.World.Bank.payload.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountInfo {

    private String accountName; //username

    private BigDecimal accountBalance;

    private String accountNumber;

    private String bankName;
}
