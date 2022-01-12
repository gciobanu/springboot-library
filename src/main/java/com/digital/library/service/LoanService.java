package com.digital.library.service;

import com.digital.library.data.payloads.LoanRequest;
import com.digital.library.exceptions.OutstandingBookException;
import com.digital.library.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Component;

@Component
public interface LoanService {

    void loanBook(LoanRequest loanRequest) throws OutstandingBookException, ResourceNotFoundException;

    void returnBook(LoanRequest loanRequest) throws ResourceNotFoundException;
}
