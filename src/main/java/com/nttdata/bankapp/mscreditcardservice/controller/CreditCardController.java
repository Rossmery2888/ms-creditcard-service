package com.nttdata.bankapp.mscreditcardservice.controller;

import com.nttdata.bankapp.mscreditcardservice.dto.CreditCardBalanceDto;
import com.nttdata.bankapp.mscreditcardservice.dto.CreditCardDto;
import com.nttdata.bankapp.mscreditcardservice.service.CreditCardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.math.BigDecimal;

/**
 * Controlador para operaciones con tarjetas de crédito.
 */
@RestController
@RequestMapping("/credit-cards")
@RequiredArgsConstructor
@Slf4j
public class CreditCardController {

    private final CreditCardService creditCardService;

    /**
     * Obtiene todas las tarjetas de crédito.
     * @return Flux de CreditCardDto
     */
    @GetMapping
    public Flux<CreditCardDto> getAll() {
        log.info("GET /credit-cards");
        return creditCardService.findAll();
    }

    /**
     * Obtiene una tarjeta de crédito por su ID.
     * @param id ID de la tarjeta de crédito
     * @return Mono de CreditCardDto
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<CreditCardDto>> getById(@PathVariable String id) {
        log.info("GET /credit-cards/{}", id);
        return creditCardService.findById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Obtiene las tarjetas de crédito de un cliente.
     * @param customerId ID del cliente
     * @return Flux de CreditCardDto
     */
    @GetMapping("/customer/{customerId}")
    public Flux<CreditCardDto> getByCustomerId(@PathVariable String customerId) {
        log.info("GET /credit-cards/customer/{}", customerId);
        return creditCardService.findByCustomerId(customerId);
    }

    /**
     * Obtiene una tarjeta de crédito por su número.
     * @param cardNumber Número de tarjeta
     * @return Mono de CreditCardDto
     */
    @GetMapping("/number/{cardNumber}")
    public Mono<ResponseEntity<CreditCardDto>> getByCardNumber(@PathVariable String cardNumber) {
        log.info("GET /credit-cards/number/{}", cardNumber);
        return creditCardService.findByCardNumber(cardNumber)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Consulta el saldo de una tarjeta de crédito.
     * @param id ID de la tarjeta de crédito
     * @return Mono de CreditCardBalanceDto
     */
    @GetMapping("/{id}/balance")
    public Mono<ResponseEntity<CreditCardBalanceDto>> getBalance(@PathVariable String id) {
        log.info("GET /credit-cards/{}/balance", id);
        return creditCardService.getBalance(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Crea una nueva tarjeta de crédito.
     * @param creditCardDto DTO con los datos de la tarjeta
     * @return Mono de CreditCardDto
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<CreditCardDto> create(@Valid @RequestBody CreditCardDto creditCardDto) {
        log.info("POST /credit-cards");
        return creditCardService.save(creditCardDto);
    }

    /**
     * Actualiza una tarjeta de crédito existente.
     * @param id ID de la tarjeta de crédito
     * @param creditCardDto DTO con los datos a actualizar
     * @return Mono de CreditCardDto
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<CreditCardDto>> update(@PathVariable String id, @Valid @RequestBody CreditCardDto creditCardDto) {
        log.info("PUT /credit-cards/{}", id);
        return creditCardService.update(id, creditCardDto)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Elimina una tarjeta de crédito.
     * @param id ID de la tarjeta de crédito
     * @return Mono<Void>
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable String id) {
        log.info("DELETE /credit-cards/{}", id);
        return creditCardService.delete(id);
    }

    /**
     * Registra un consumo en una tarjeta de crédito.
     * @param id ID de la tarjeta de crédito
     * @param amount Monto del consumo
     * @return Mono de CreditCardDto
     */
    @PutMapping("/{id}/consumption")
    public Mono<ResponseEntity<CreditCardDto>> registerConsumption(
            @PathVariable String id,
            @RequestParam BigDecimal amount) {
        log.info("PUT /credit-cards/{}/consumption with amount: {}", id, amount);
        return creditCardService.registerConsumption(id, amount)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Realiza un pago a una tarjeta de crédito.
     * @param id ID de la tarjeta de crédito
     * @param amount Monto del pago
     * @return Mono de CreditCardDto
     */
    @PutMapping("/{id}/payment")
    public Mono<ResponseEntity<CreditCardDto>> payBalance(
            @PathVariable String id,
            @RequestParam BigDecimal amount) {
        log.info("PUT /credit-cards/{}/payment with amount: {}", id, amount);
        return creditCardService.payBalance(id, amount)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}