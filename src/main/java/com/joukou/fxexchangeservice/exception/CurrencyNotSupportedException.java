package com.joukou.fxexchangeservice.exception;

public class CurrencyNotSupportedException extends RuntimeException {

    public CurrencyNotSupportedException(String message) {
        super(message);
    }
}
