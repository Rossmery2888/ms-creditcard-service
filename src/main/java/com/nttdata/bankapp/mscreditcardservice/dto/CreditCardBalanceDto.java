package com.nttdata.bankapp.mscreditcardservice.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO para la información de saldo de tarjeta de crédito.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreditCardBalanceDto {
    private String creditCardId;
    private String cardNumber;
    private BigDecimal creditLimit;
    private BigDecimal availableBalance;
    private BigDecimal usedBalance;
}

