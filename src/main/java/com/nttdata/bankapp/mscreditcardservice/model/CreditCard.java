package com.nttdata.bankapp.mscreditcardservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Modelo que representa una tarjeta de crédito.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "creditCards")
public class CreditCard {
    @Id
    private String id;

    @Indexed(unique = true)
    private String cardNumber;
    private String customerId;
    private CustomerType customerType; // PERSONAL, BUSINESS
    private BigDecimal creditLimit;
    private BigDecimal availableBalance;
    private LocalDate expirationDate;
    private String cvv;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
