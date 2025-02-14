package com.example.mscreditcardservice.dto;

import com.example.mscreditcardservice.model.enums.CreditCardType;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
public class CreditCardDTO {
    private String id;
    @NotBlank
    private String customerId;
    @NotNull
    private CreditCardType creditType;
    @NotNull
    @Positive
    private BigDecimal amount;
    @NotNull
    @Positive
    private BigDecimal monthlyPayment;
    @NotNull
    private Integer numberOfPayments;
}