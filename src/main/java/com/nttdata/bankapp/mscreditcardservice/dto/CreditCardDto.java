package com.nttdata.bankapp.mscreditcardservice.dto;
import com.nttdata.bankapp.mscreditcardservice.model.CustomerType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO para la transferencia de datos de tarjetas de crédito.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreditCardDto {
    private String id;
    private String cardNumber;

    @NotBlank(message = "Customer ID is required")
    private String customerId;

    @NotNull(message = "Customer type is required")
    private CustomerType customerType;

    @NotNull(message = "Credit limit is required")
    @DecimalMin(value = "0.01", message = "Credit limit must be greater than 0")
    private BigDecimal creditLimit;

    private BigDecimal availableBalance;
    private LocalDate expirationDate;
    private String cvv;
}