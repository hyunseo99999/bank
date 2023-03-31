package com.bank.exception.handler.ex;

public class CustomApiException extends RuntimeException {
    public CustomApiException(String message) {
        super(message);
    }
}
