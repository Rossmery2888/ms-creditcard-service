package com.nttdata.bankapp.mscreditcardservice.service.impl;

import com.nttdata.bankapp.mscreditcardservice.client.CustomerService;
import com.nttdata.bankapp.mscreditcardservice.dto.CreditCardBalanceDto;
import com.nttdata.bankapp.mscreditcardservice.dto.CreditCardDto;
import com.nttdata.bankapp.mscreditcardservice.exception.CreditCardNotFoundException;
import com.nttdata.bankapp.mscreditcardservice.exception.CustomerNotFoundException;
import com.nttdata.bankapp.mscreditcardservice.model.CreditCard;
import com.nttdata.bankapp.mscreditcardservice.repository.CreditCardRepository;
import com.nttdata.bankapp.mscreditcardservice.service.CreditCardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;

/**
 * Implementación de los servicios para operaciones con tarjetas de crédito.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CreditCardServiceImpl implements CreditCardService {

    private final CreditCardRepository creditCardRepository;
    private final CustomerService customerService;

    @Override
    public Flux<CreditCardDto> findAll() {
        log.info("Finding all credit cards");
        return creditCardRepository.findAll()
                .map(this::mapToDto);
    }

    @Override
    public Mono<CreditCardDto> findById(String id) {
        log.info("Finding credit card by id: {}", id);
        return creditCardRepository.findById(id)
                .map(this::mapToDto)
                .switchIfEmpty(Mono.error(new CreditCardNotFoundException("Credit card not found with id: " + id)));
    }

    @Override
    public Flux<CreditCardDto> findByCustomerId(String customerId) {
        log.info("Finding credit cards by customer id: {}", customerId);
        return creditCardRepository.findByCustomerId(customerId)
                .map(this::mapToDto);
    }

    @Override
    public Mono<CreditCardDto> findByCardNumber(String cardNumber) {
        log.info("Finding credit card by card number: {}", cardNumber);
        return creditCardRepository.findByCardNumber(cardNumber)
                .map(this::mapToDto)
                .switchIfEmpty(Mono.error(new CreditCardNotFoundException("Credit card not found with card number: " + cardNumber)));
    }

    @Override
    public Mono<CreditCardDto> save(CreditCardDto creditCardDto) {
        log.info("Saving new credit card: {}", creditCardDto);

        // Verificar si el cliente existe
        return customerService.customerExists(creditCardDto.getCustomerId())
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new CustomerNotFoundException("Customer not found with id: " + creditCardDto.getCustomerId()));
                    }

                    CreditCard creditCard = mapToEntity(creditCardDto);

                    // Generar número de tarjeta, fecha de expiración y CVV
                    creditCard.setCardNumber(generateCardNumber());
                    creditCard.setExpirationDate(LocalDate.now().plusYears(4));
                    creditCard.setCvv(generateCVV());

                    // Establecer saldo disponible igual al límite de crédito
                    creditCard.setAvailableBalance(creditCard.getCreditLimit());
                    creditCard.setCreatedAt(LocalDateTime.now());
                    creditCard.setUpdatedAt(LocalDateTime.now());

                    return creditCardRepository.save(creditCard).map(this::mapToDto);
                });
    }

    @Override
    public Mono<CreditCardDto> update(String id, CreditCardDto creditCardDto) {
        log.info("Updating credit card id: {}", id);
        return creditCardRepository.findById(id)
                .switchIfEmpty(Mono.error(new CreditCardNotFoundException("Credit card not found with id: " + id)))
                .flatMap(existingCard -> {
                    // No permitir cambiar campos críticos como cliente
                    if (creditCardDto.getCustomerId() != null && !creditCardDto.getCustomerId().equals(existingCard.getCustomerId())) {
                        return Mono.error(new IllegalArgumentException("Cannot change credit card owner"));
                    }

                    if (creditCardDto.getCustomerType() != null && !creditCardDto.getCustomerType().equals(existingCard.getCustomerType())) {
                        return Mono.error(new IllegalArgumentException("Cannot change customer type"));
                    }

                    // Actualizar límite de crédito si se proporciona
                    if (creditCardDto.getCreditLimit() != null) {
                        BigDecimal limitDifference = creditCardDto.getCreditLimit().subtract(existingCard.getCreditLimit());
                        existingCard.setCreditLimit(creditCardDto.getCreditLimit());
                        // Actualizar también el saldo disponible
                        existingCard.setAvailableBalance(existingCard.getAvailableBalance().add(limitDifference));
                    }

                    existingCard.setUpdatedAt(LocalDateTime.now());

                    return creditCardRepository.save(existingCard);
                })
                .map(this::mapToDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        log.info("Deleting credit card id: {}", id);
        return creditCardRepository.findById(id)
                .switchIfEmpty(Mono.error(new CreditCardNotFoundException("Credit card not found with id: " + id)))
                .flatMap(creditCard -> creditCardRepository.deleteById(id));
    }

    @Override
    public Mono<CreditCardBalanceDto> getBalance(String id) {
        log.info("Getting balance for credit card id: {}", id);
        return creditCardRepository.findById(id)
                .switchIfEmpty(Mono.error(new CreditCardNotFoundException("Credit card not found with id: " + id)))
                .map(creditCard -> {
                    BigDecimal usedBalance = creditCard.getCreditLimit().subtract(creditCard.getAvailableBalance());

                    return CreditCardBalanceDto.builder()
                            .creditCardId(creditCard.getId())
                            .cardNumber(creditCard.getCardNumber())
                            .creditLimit(creditCard.getCreditLimit())
                            .availableBalance(creditCard.getAvailableBalance())
                            .usedBalance(usedBalance)
                            .build();
                });
    }

    @Override
    public Mono<CreditCardDto> registerConsumption(String id, BigDecimal amount) {
        log.info("Registering consumption for credit card id: {} with amount: {}", id, amount);
        return creditCardRepository.findById(id)
                .switchIfEmpty(Mono.error(new CreditCardNotFoundException("Credit card not found with id: " + id)))
                .flatMap(creditCard -> {
                    // Validar que el monto no sea mayor que el saldo disponible
                    if (amount.compareTo(creditCard.getAvailableBalance()) > 0) {
                        return Mono.error(new IllegalArgumentException("Consumption amount exceeds available balance"));
                    }

                    // Actualizar saldo disponible
                    BigDecimal newAvailableBalance = creditCard.getAvailableBalance().subtract(amount);
                    creditCard.setAvailableBalance(newAvailableBalance);
                    creditCard.setUpdatedAt(LocalDateTime.now());

                    return creditCardRepository.save(creditCard);
                })
                .map(this::mapToDto);
    }

    @Override
    public Mono<CreditCardDto> payBalance(String id, BigDecimal amount) {
        log.info("Making payment to credit card id: {} with amount: {}", id, amount);
        return creditCardRepository.findById(id)
                .switchIfEmpty(Mono.error(new CreditCardNotFoundException("Credit card not found with id: " + id)))
                .flatMap(creditCard -> {
                    // Validar que el monto no sea mayor que la deuda
                    BigDecimal usedBalance = creditCard.getCreditLimit().subtract(creditCard.getAvailableBalance());
                    if (amount.compareTo(usedBalance) > 0) {
                        return Mono.error(new IllegalArgumentException("Payment amount exceeds used balance"));
                    }

                    // Actualizar saldo disponible
                    BigDecimal newAvailableBalance = creditCard.getAvailableBalance().add(amount);
                    creditCard.setAvailableBalance(newAvailableBalance);
                    creditCard.setUpdatedAt(LocalDateTime.now());

                    return creditCardRepository.save(creditCard);
                })
                .map(this::mapToDto);
    }

    /**
     * Genera un número de tarjeta de crédito aleatorio.
     * @return String con el número de tarjeta
     */
    private String generateCardNumber() {
        // Formato: XXXX-XXXX-XXXX-XXXX
        return String.format("5%03d-%04d-%04d-%04d",
                new Random().nextInt(1000),
                new Random().nextInt(10000),
                new Random().nextInt(10000),
                new Random().nextInt(10000));
    }

    /**
     * Genera un código CVV aleatorio.
     * @return String con el código CVV
     */
    private String generateCVV() {
        return String.format("%03d", new Random().nextInt(1000));
    }

    /**
     * Convierte una entidad CreditCard a DTO.
     * @param creditCard Entidad a convertir
     * @return CreditCardDto
     */
    private CreditCardDto mapToDto(CreditCard creditCard) {
        return CreditCardDto.builder()
                .id(creditCard.getId())
                .cardNumber(creditCard.getCardNumber())
                .customerId(creditCard.getCustomerId())
                .customerType(creditCard.getCustomerType())
                .creditLimit(creditCard.getCreditLimit())
                .availableBalance(creditCard.getAvailableBalance())
                .expirationDate(creditCard.getExpirationDate())
                .cvv(creditCard.getCvv())
                .build();
    }

    /**
     * Convierte un DTO a entidad CreditCard.
     * @param creditCardDto DTO a convertir
     * @return CreditCard
     */
    private CreditCard mapToEntity(CreditCardDto creditCardDto) {
        return CreditCard.builder()
                .customerId(creditCardDto.getCustomerId())
                .customerType(creditCardDto.getCustomerType())
                .creditLimit(creditCardDto.getCreditLimit())
                .build();
    }
}
