package com.digital.library.data.payloads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
@Setter
public class CategoryRequest {

    private Integer categoryId;

    @NotNull
    @NotBlank
    private String name;
}
