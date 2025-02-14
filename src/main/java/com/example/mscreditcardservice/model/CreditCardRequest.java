package com.example.mscreditcardservice.model;

import com.example.mscreditcardservice.model.enums.CreditCardType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditCardRequest {
    private String customerId;
    private CreditCardType type;
    private BigDecimal creditLimit;
}
