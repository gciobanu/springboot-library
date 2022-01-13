package com.digital.library.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ResourceNotFoundException extends Throwable {
    private String resourceType;
    private String resourceField;
    private String resourceFieldValue;

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
