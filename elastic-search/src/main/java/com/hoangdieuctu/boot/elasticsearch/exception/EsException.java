package com.hoangdieuctu.boot.elasticsearch.exception;

public class EsException extends RuntimeException {

    public EsException(String message) {
        super(message);
    }

    public EsException(String message, Throwable cause) {
        super(message, cause);
    }
}
