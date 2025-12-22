package com.ikram.gestiondestock.exception;

import lombok.Getter;

public class InvalidOperationException extends RuntimeException {

    @Getter
    private ErrorCodes errorCodes;

    public InvalidOperationException(String message, ErrorCodes errorCode) {
        super(message);
        this.errorCodes = errorCode;
    }

    public ErrorCodes getErrorCode() {
        return errorCodes;
    }
}
