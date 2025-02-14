package com.example.mscreditcardservice.repository;

import com.example.mscreditcardservice.model.CreditCard;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditCardRepository extends ReactiveCrudRepository<CreditCard, String> {
}