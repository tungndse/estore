package com.coldev.estore.config.exception.general;

public class DuplicatedException extends RuntimeException{

    public DuplicatedException(String message) {
        super(message);
    }
}
