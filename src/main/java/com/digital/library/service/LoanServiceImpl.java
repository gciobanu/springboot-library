package com.digital.library.service;

import com.digital.library.data.payloads.LoanRequest;
import com.digital.library.data.repository.LoanRepository;
import com.digital.library.exceptions.OutstandingBookException;
import com.digital.library.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoanServiceImpl implements LoanService {
    @Autowired
    LoanRepository loanRepository;

    @Override
    public void loanBook(LoanRequest loanRequest) throws OutstandingBookException, ResourceNotFoundException {

    }

    @Override
    public void returnBook(LoanRequest loanRequest) throws ResourceNotFoundException {

    }
}
