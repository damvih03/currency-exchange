package com.damvih.exceptions;

public class DatabaseOperationException extends RuntimeException {

    private static final String MESSAGE = "Что-то случилось с базой данных";

    public DatabaseOperationException() {
        super(MESSAGE);
    }

}
