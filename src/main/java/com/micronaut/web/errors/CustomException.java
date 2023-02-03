package com.micronaut.web.errors;

public class CustomException extends RuntimeException {

    public CustomException() {
        super("This is a custom exception");
    }
}
