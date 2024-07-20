package com.damvih.exceptions;

public class MissedParameterException extends RuntimeException {

    private static final String MESSAGE = "Missed parameter (%s)";

    public MissedParameterException(String missedParameter) {
        super(String.format(MESSAGE, missedParameter));
    }

}
