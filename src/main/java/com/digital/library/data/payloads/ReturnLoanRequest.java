package com.digital.library.data.payloads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReturnLoanRequest  extends LoanRequest {
    @NotNull
    @NotBlank
    private LocalDate returnLoanDate;
}
