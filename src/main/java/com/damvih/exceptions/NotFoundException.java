package com.damvih.exceptions;

public class NotFoundException extends RuntimeException {

    private static final String MESSAGE = "Resource not found (%s)";

    public NotFoundException(String resourceName) {
        super(String.format(MESSAGE, resourceName));
    }

}
