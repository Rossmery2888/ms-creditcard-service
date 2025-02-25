package com.example.mscreditcardservice.controller;

import com.example.mscreditcardservice.model.*;
import com.example.mscreditcardservice.service.CreditCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/credit-cards")
@RequiredArgsConstructor
public class CreditCardController {
    private final CreditCardService creditCardService;

    @PostMapping
    public Mono<CreditCard> createCreditCard(@RequestBody CreditCardRequest request) {
        return creditCardService.createCreditCard(request);
    }

    @PostMapping("/{cardId}/consume")
    public Mono<CreditCard> consume(@PathVariable String cardId, @RequestBody ConsumptionRequest request) {
        return creditCardService.consume(cardId, request);
    }

    @PostMapping("/{cardId}/pay")
    public Mono<CreditCard> payBalance(@PathVariable String cardId, @RequestBody PaymentRequest request) {
        return creditCardService.payBalance(cardId, request);
    }

    @GetMapping("/{cardId}/consumptions")
    public Flux<ConsumptionRecord> getConsumptions(@PathVariable String cardId) {
        return creditCardService.getConsumptions(cardId);
    }

    @GetMapping("/customer/{customerId}/active")
    public Mono<Boolean> hasActiveCard(@PathVariable String customerId) {
        return creditCardService.hasActiveCard(customerId);
    }

    @GetMapping("/customer/{customerId}")
    public Flux<CreditCard> getCustomerCards(@PathVariable String customerId) {
        return creditCardService.getCustomerCards(customerId);
    }

    // New endpoint for last 10 movements
    @GetMapping("/{cardId}/last-movements")
    public Flux<ConsumptionRecord> getLastTenMovements(@PathVariable String cardId) {
        return creditCardService.getLastTenMovements(cardId);
    }
}



