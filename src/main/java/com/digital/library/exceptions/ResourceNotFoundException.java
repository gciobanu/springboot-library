package com.digital.library.exceptions;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ResourceNotFoundException extends Throwable {
    private String resourceType;
    private String resourceField;
    private String resourceFieldValue;

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resourceType, String resourceField, String resourceFieldValue) {
        super("Not Found the resource " + resourceType +
                " with property:" + resourceField +
                " = " + resourceFieldValue);
    }
}
