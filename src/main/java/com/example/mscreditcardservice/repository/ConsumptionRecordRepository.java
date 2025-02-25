package com.example.mscreditcardservice.repository;

import com.example.mscreditcardservice.model.ConsumptionRecord;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ConsumptionRecordRepository extends ReactiveCrudRepository<ConsumptionRecord, String> {
    Flux<ConsumptionRecord> findByCreditCardId(String creditCardId);
    Flux<ConsumptionRecord> findByCreditCardIdOrderByTimestampDesc(String creditCardId, Pageable pageable);
}
