package com.example.mscreditcardservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "consumption_records")
public class ConsumptionRecord {
    @Id
    private String id;
    private String creditCardId;
    private BigDecimal amount;
    private LocalDateTime timestamp;
}
