package com.digital.library.exceptions;

public class OutstandingBookException extends Throwable {
    private String message;

    public OutstandingBookException(String message) {
        super(message);
    }
}
