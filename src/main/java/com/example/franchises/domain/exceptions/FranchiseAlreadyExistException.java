package com.example.franchises.domain.exceptions;

public class FranchiseAlreadyExistException extends RuntimeException {
    public FranchiseAlreadyExistException(String message) {
        super(message);
    }
}
