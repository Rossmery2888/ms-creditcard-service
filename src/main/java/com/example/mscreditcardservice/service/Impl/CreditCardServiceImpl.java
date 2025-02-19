package com.example.mscreditcardservice.service.Impl;

import com.example.mscreditcardservice.model.*;
import com.example.mscreditcardservice.model.enums.CreditCardStatus;
import com.example.mscreditcardservice.repository.ConsumptionRecordRepository;
import com.example.mscreditcardservice.repository.CreditCardRepository;
import com.example.mscreditcardservice.service.CreditCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
class CreditCardServiceImpl implements CreditCardService {
    private final CreditCardRepository creditCardRepository;
    private final ConsumptionRecordRepository consumptionRecordRepository;

    @Override
    public Mono<CreditCard> createCreditCard(CreditCardRequest request) {
        CreditCard card = new CreditCard(UUID.randomUUID().toString(), request.getCustomerId(), request.getType(), request.getCreditLimit(), BigDecimal.ZERO, CreditCardStatus.ACTIVE);
        return creditCardRepository.save(card);
    }

    @Override
    public Mono<CreditCard> consume(String cardId, ConsumptionRequest request) {
        return creditCardRepository.findById(cardId)
                .flatMap(card -> {
                    BigDecimal newBalance = card.getBalance().add(request.getAmount());
                    if (newBalance.compareTo(card.getCreditLimit()) > 0) {
                        return Mono.error(new IllegalStateException("Credit limit exceeded"));
                    }
                    card.setBalance(newBalance);
                    ConsumptionRecord record = new ConsumptionRecord(UUID.randomUUID().toString(), cardId, request.getAmount(), LocalDateTime.now());
                    return consumptionRecordRepository.save(record).then(creditCardRepository.save(card));
                });
    }

    @Override
    public Mono<CreditCard> payBalance(String cardId, PaymentRequest request) {
        return creditCardRepository.findById(cardId)
                .flatMap(card -> {
                    BigDecimal newBalance = card.getBalance().subtract(request.getPaymentAmount());
                    if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
                        return Mono.error(new IllegalStateException("Payment exceeds balance"));
                    }
                    card.setBalance(newBalance);
                    return creditCardRepository.save(card);
                });
    }
    @Override
    public Mono<Boolean> hasActiveCard(String customerId) {
        return creditCardRepository.existsByCustomerIdAndStatus(customerId, CreditCardStatus.ACTIVE);
    }

    @Override
    public Flux<CreditCard> getCustomerCards(String customerId) {
        return creditCardRepository.findByCustomerId(customerId);
    }

    @Override
    public Flux<ConsumptionRecord> getConsumptions(String cardId) {
        return consumptionRecordRepository.findByCreditCardId(cardId);
    }
}


