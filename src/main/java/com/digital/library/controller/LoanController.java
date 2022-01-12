package com.digital.library.controller;

import com.digital.library.data.payloads.LoanRequest;
import com.digital.library.data.payloads.MessageResponse;
import com.digital.library.data.payloads.ReturnLoanRequest;
import com.digital.library.exceptions.OutstandingBookException;
import com.digital.library.exceptions.ResourceNotFoundException;
import com.digital.library.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/library/loan")
public class LoanController {
    @Autowired
    LoanService loanService;

    @PostMapping("/new")
    public ResponseEntity<MessageResponse> loanBook(@RequestBody LoanRequest request) {
        try {
            this.loanService.loanBook(request);
        } catch (OutstandingBookException e) {
            return new ResponseEntity<>(new MessageResponse("You have outstanding book(s) to return!"), HttpStatus.CONFLICT);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new MessageResponse("Book or user member not found."), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new MessageResponse("Book(s) loaned."), HttpStatus.CREATED);
    }

    @PostMapping("/return")
    public ResponseEntity<MessageResponse> returnBook(@RequestBody ReturnLoanRequest request) {
        try {
            this.loanService.returnBook(request);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new MessageResponse("Book or user member not found."), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new MessageResponse("Book(s) return."), HttpStatus.CREATED);
    }
}
