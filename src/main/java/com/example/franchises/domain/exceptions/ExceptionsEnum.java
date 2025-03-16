package com.example.franchises.domain.exceptions;

public enum ExceptionsEnum {
    ALREADY_EXIST_FRANCHISE("Already exist franchise name"),
    FRANCHISE_NAME_MANDATORY("Franchise name is mandatory"),
    FRANCHISE_ID_REQUIRED("Franchise id is mandatory"),
    FRANCHISE_NOT_FOUND("Franchise not found"),
    ALREADY_EXIST_BRANCH( "Already exist branch name"),
    BRANCH_NAME_MANDATORY("Branch name is mandatory"),

    ;

    private final String message;

    ExceptionsEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
