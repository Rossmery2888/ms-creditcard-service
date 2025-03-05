package com.nttdata.bankapp.mscreditcardservice.exception;

/**
 * Excepción personalizada para tarjeta de crédito no encontrada.
 */
public class CreditCardNotFoundException extends RuntimeException {
    public CreditCardNotFoundException(String message) {
        super(message);
    }
}
