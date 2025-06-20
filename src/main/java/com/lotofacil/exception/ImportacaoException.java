package com.lotofacil.exception;

public class ImportacaoException extends RuntimeException {

    public ImportacaoException(String message) {
        super(message);
    }

    public ImportacaoException(String message, Throwable cause) {
        super(message, cause);
    }
}

