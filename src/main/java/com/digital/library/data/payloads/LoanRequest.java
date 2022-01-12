package com.digital.library.data.payloads;

import com.digital.library.data.model.Book;
import com.digital.library.data.model.UserMember;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoanRequest {
    @NotNull
    @NotBlank
    private List<Book> books;

    @NotNull
    @NotBlank
    private UserMember userMember;
}
