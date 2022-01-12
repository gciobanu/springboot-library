package com.digital.library.service;

import com.digital.library.data.model.Book;
import com.digital.library.data.payloads.BookRequest;
import com.digital.library.data.payloads.CategoryRequest;
import com.digital.library.data.payloads.MessageResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface BookService {

    MessageResponse save(BookRequest book);

    MessageResponse update(Integer bookId, BookRequest book);

    void delete(Integer bookId);

    Book findById(Integer bookId);

    List<Book> findByCategory(CategoryRequest category);

    //find by title and/or author
    List<Book> find(BookRequest book);

    List<Book> findAll();

}
