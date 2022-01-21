package com.digital.library.controller;

import com.digital.library.data.model.Book;
import com.digital.library.data.model.Category;
import com.digital.library.data.model.UserMember;
import com.digital.library.data.payloads.LoanRequest;
import com.digital.library.data.payloads.MessageResponse;
import com.digital.library.data.payloads.ReturnLoanRequest;
import com.digital.library.exceptions.ApiException;
import com.digital.library.exceptions.OutstandingBookException;
import com.digital.library.exceptions.ResourceNotFoundException;
import com.digital.library.service.LoanService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@RunWith(SpringRunner.class)
//@WebMvcTest(LoanController.class)
public class LoanControllerTest {
    //@Autowired
    private MockMvc mvc;

    //@MockBean
    private LoanService service;

    //@Test
    public void loanBook_OK() {

        Book book = new Book();
        book.setTitle("Title 1");
        book.setAuthor("Author");
        book.setId(1);
        Category cat = new Category();
        cat.setId(1);
        cat.setName("Science");
        book.setCategorySet(Stream.of(cat).collect(Collectors.toSet()));

        LoanRequest req = new LoanRequest();
        UserMember user = new UserMember();
        user.setId(1);
        req.setUserMember(user);
        req.setBooks(Arrays.asList(book));

        try {
            MessageResponse rsp = new MessageResponse("Book(s) loaned.");
            given(service.loanBook(req)).willReturn(rsp);
            ObjectMapper objectMapper = new ObjectMapper();

            mvc.perform(post("/library/loan/new")
                            .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(req)))
                  //  .andExpect(status().isCreated())
            ;
        } catch (ApiException e) {
            e.printStackTrace();
            Assert.fail();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            Assert.fail();
        } catch (OutstandingBookException e) {
            e.printStackTrace();
            Assert.fail();
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
            Assert.fail();
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

   // @Test
    public void returnBook_OK()
            throws Exception, ApiException {

        Book book = new Book();
        book.setTitle("Title 1");
        book.setAuthor("Author");
        Category cat = new Category();
        cat.setId(1);
        cat.setName("Science");
        book.setCategorySet(Stream.of(cat).collect(Collectors.toSet()));

        ReturnLoanRequest req = new ReturnLoanRequest();
        UserMember user = new UserMember();
        user.setId(1);
        req.setUserMember(user);
        req.setBooks(Arrays.asList(book));

        MessageResponse rsp = new MessageResponse("Book(s) return confirmed.");

        try {
            given(service.returnBook(req)).willReturn(rsp);
            ObjectMapper objectMapper = new ObjectMapper();

            mvc.perform(put("/library/loan/return")
                            .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(req)))
                   // .andExpect(status().isOk())
            ;
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
            Assert.fail();
        }


    }
}
