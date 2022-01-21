package com.digital.library.controller;

import com.digital.library.data.payloads.LoanRequest;
import com.digital.library.data.payloads.MessageResponse;
import com.digital.library.data.payloads.ReturnLoanRequest;
import com.digital.library.exceptions.ApiException;
import com.digital.library.exceptions.OutstandingBookException;
import com.digital.library.exceptions.ResourceNotFoundException;
import com.digital.library.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/library/loan")
public class LoanController {
    @Autowired
    LoanService loanService;

    @PostMapping("/new")
    public ResponseEntity<MessageResponse> loanBook(@RequestBody LoanRequest request) {
        if (request == null) {
            return new ResponseEntity<>(new MessageResponse("No request data was sent!"), HttpStatus.BAD_REQUEST);
        }
        if (request.getUserMember() == null || request.getUserMember().getId() == null) {
            return new ResponseEntity<>(new MessageResponse("No user member ID was sent!"), HttpStatus.BAD_REQUEST);
        }
        try {
            MessageResponse response = this.loanService.loanBook(request);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (OutstandingBookException e) {
            return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.CONFLICT);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (ApiException e) {
            return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/return")
    public ResponseEntity<MessageResponse> returnBook(@RequestBody ReturnLoanRequest request) {
        try {
            return new ResponseEntity<>(this.loanService.returnBook(request), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (ApiException e) {
            return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
