package com.example.mscreditcardservice.repository;

import com.example.mscreditcardservice.model.CreditCard;
import com.example.mscreditcardservice.model.enums.CreditCardStatus;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CreditCardRepository extends ReactiveCrudRepository<CreditCard, String> {
    Flux<CreditCard> findByCustomerId(String customerId);
    Mono<Boolean> existsByCustomerIdAndStatus(String customerId, CreditCardStatus status);
}
