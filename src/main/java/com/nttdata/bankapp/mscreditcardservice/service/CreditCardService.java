package com.nttdata.bankapp.mscreditcardservice.service;
import com.nttdata.bankapp.mscreditcardservice.dto.CreditCardBalanceDto;
import com.nttdata.bankapp.mscreditcardservice.dto.CreditCardDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

/**
 * Interfaz que define los servicios para operaciones con tarjetas de crédito.
 */
public interface CreditCardService {
    Flux<CreditCardDto> findAll();
    Mono<CreditCardDto> findById(String id);
    Flux<CreditCardDto> findByCustomerId(String customerId);
    Mono<CreditCardDto> findByCardNumber(String cardNumber);
    Mono<CreditCardDto> save(CreditCardDto creditCardDto);
    Mono<CreditCardDto> update(String id, CreditCardDto creditCardDto);
    Mono<Void> delete(String id);
    Mono<CreditCardBalanceDto> getBalance(String id);
    Mono<CreditCardDto> registerConsumption(String id, BigDecimal amount);
    Mono<CreditCardDto> payBalance(String id, BigDecimal amount);
}
