package com.example.franchises.domain.exceptions;

public class IncorrectIdException extends RuntimeException {
    public IncorrectIdException(String message) {
        super(message);
    }

    public static IncorrectIdException of(String message) {
        return new IncorrectIdException(message);
    }

}
