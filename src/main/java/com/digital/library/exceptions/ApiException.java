package com.digital.library.exceptions;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ApiException extends Throwable {
    private String message;

    public ApiException(String message) {
        super(message);
    }
}
