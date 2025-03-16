package com.example.franchises.domain.exceptions;

public class BranchAlreadyExistException extends RuntimeException {
    public BranchAlreadyExistException(String message) {
        super(message);
    }
}
