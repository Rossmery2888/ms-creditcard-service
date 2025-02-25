package com.example.mscreditcardservice.service;


import com.example.mscreditcardservice.model.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import com.example.mscreditcardservice.model.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CreditCardService {
    Mono<CreditCard> createCreditCard(CreditCardRequest request);
    Mono<CreditCard> consume(String cardId, ConsumptionRequest request);
    Mono<CreditCard> payBalance(String cardId, PaymentRequest request);
    Flux<ConsumptionRecord> getConsumptions(String cardId);
    Flux<ConsumptionRecord> getLastTenMovements(String cardId);
    Mono<Boolean> hasActiveCard(String customerId);
    Flux<CreditCard> getCustomerCards(String customerId);
}