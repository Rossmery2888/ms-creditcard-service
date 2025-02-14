package com.example.mscreditcardservice.model;


import com.example.mscreditcardservice.model.enums.CreditCardStatus;
import com.example.mscreditcardservice.model.enums.CreditCardType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditCard {
    private String id;
    private String customerId;
    private CreditCardType type;
    private BigDecimal creditLimit;
    private BigDecimal balance;
    private CreditCardStatus status;
}


