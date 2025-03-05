package com.nttdata.bankapp.mscreditcardservice.exception;

/**
 * Excepción personalizada para cliente no encontrado.
 */
public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(String message) {
        super(message);
    }
}
