package com.digital.library.service;

import com.digital.library.data.payloads.LoanRequest;
import com.digital.library.data.payloads.MessageResponse;
import com.digital.library.data.payloads.ReturnLoanRequest;
import com.digital.library.exceptions.ApiException;
import com.digital.library.exceptions.OutstandingBookException;
import com.digital.library.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Component;

@Component
public interface LoanService {

    MessageResponse loanBook(LoanRequest loanRequest)
            throws OutstandingBookException, ResourceNotFoundException, ApiException;

    MessageResponse returnBook(ReturnLoanRequest loanRequest)
            throws ResourceNotFoundException, ApiException;
}
