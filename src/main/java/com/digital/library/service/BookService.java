package com.digital.library.service;

import com.digital.library.data.model.Book;
import com.digital.library.data.payloads.BookRequest;
import com.digital.library.data.payloads.MessageResponse;
import com.digital.library.data.payloads.SearchResponse;
import com.digital.library.exceptions.ApiException;
import com.digital.library.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface BookService {

    Book save(BookRequest book) throws ApiException;

    MessageResponse update(Integer bookId, BookRequest book) throws  ResourceNotFoundException, ApiException;

    MessageResponse delete(Integer bookId) throws ApiException ;

    Book findById(Integer bookId) throws ResourceNotFoundException;

    SearchResponse search(String title, String author, Integer pageNo, Integer pageSize, String sortBy);

    List<Book> findAll();

}
