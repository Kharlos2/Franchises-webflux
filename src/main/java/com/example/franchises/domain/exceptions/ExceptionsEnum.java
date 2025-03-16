package com.example.franchises.domain.exceptions;

public enum ExceptionsEnum {
    ALREADY_EXIST_FRANCHISE("Already exist franchise name"),
    FRANCHISE_NAME_MANDATORY("Franchise name is mandatory"),
    FRANCHISE_ID_REQUIRED("Franchise id is mandatory"),
    FRANCHISE_NOT_FOUND("Franchise not found"),
    ALREADY_EXIST_BRANCH( "Already exist branch name"),
    BRANCH_NAME_MANDATORY("Branch name is mandatory"),
    BRANCH_NOT_FOUND( "Branch not found" ),
    BRANCH_ID_MANDATORY("Branch id is mandatory"),
    EMPTY_PRODUCT_NAME("The name cannot be empty"),
    NEGATIVE_STOCK("Stock cannot be negative"),
    ALREADY_EXIST_PRODUCT("Already exist product name"),
    PRODUCT_NOT_FOUND("Product not found"),
    PRODUCT_ID_MANDATORY("Product id is mandatory"),
    INCORRECT_ID("The id must be a long")
    ;

    private final String message;

    ExceptionsEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
