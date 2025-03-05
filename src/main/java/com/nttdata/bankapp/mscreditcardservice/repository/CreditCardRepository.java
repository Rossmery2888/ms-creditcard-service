package com.nttdata.bankapp.mscreditcardservice.repository;

import com.nttdata.bankapp.mscreditcardservice.model.CreditCard;
import com.nttdata.bankapp.mscreditcardservice.model.CustomerType;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Repositorio para operaciones CRUD en la colección de tarjetas de crédito.
 */
@Repository
public interface CreditCardRepository extends ReactiveMongoRepository<CreditCard, String> {
    Flux<CreditCard> findByCustomerId(String customerId);
    Mono<CreditCard> findByCardNumber(String cardNumber);
    Flux<CreditCard> findByCustomerIdAndCustomerType(String customerId, CustomerType customerType);
}