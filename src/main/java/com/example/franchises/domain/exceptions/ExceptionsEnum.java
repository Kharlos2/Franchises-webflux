package com.example.franchises.domain.exceptions;

public enum ExceptionsEnum {
    ALREADY_EXIST_FRANCHISE("Already exist franchise name"),
    FRANCHISE_NAME_MANDATORY("Franchise name is mandatory")
    ;

    private final String message;

    ExceptionsEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
