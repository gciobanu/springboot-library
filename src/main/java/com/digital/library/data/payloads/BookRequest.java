package com.digital.library.data.payloads;

import com.digital.library.data.model.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookRequest {
    @NotNull
    @NotBlank
    private String title;
    @NotNull
    @NotBlank
    private String author;
    @NotNull
    @NotEmpty
    private List<Category> categories;
}
