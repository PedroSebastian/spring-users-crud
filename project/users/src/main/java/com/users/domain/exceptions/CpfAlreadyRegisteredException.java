package com.users.domain.exceptions;

public class CpfAlreadyRegisteredException extends RuntimeException {
    public CpfAlreadyRegisteredException(String message) {
        super(message);
    }
}
