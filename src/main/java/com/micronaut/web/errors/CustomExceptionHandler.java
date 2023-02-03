package com.micronaut.web.errors;

import io.micronaut.core.exceptions.ExceptionHandler;

public class CustomExceptionHandler implements ExceptionHandler<CustomException> {


    @Override
    public void handle(CustomException exception) {

    }
}
