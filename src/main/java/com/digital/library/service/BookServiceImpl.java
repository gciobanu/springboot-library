package com.digital.library.service;

import com.digital.library.data.model.Book;
import com.digital.library.data.payloads.BookRequest;
import com.digital.library.data.payloads.CategoryRequest;
import com.digital.library.data.payloads.MessageResponse;
import com.digital.library.data.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {
    @Autowired
    BookRepository bookRepository;

    @Override
    public MessageResponse save(BookRequest book) {
        return null;
    }

    @Override
    public MessageResponse update(Integer bookId, BookRequest book) {
        return null;
    }

    @Override
    public void delete(Integer bookId) {

    }

    @Override
    public Book findById(Integer bookId) {
        return null;
    }

    @Override
    public List<Book> findByCategory(CategoryRequest category) {
        return null;
    }

    @Override
    public List<Book> find(BookRequest book) {
        return null;
    }

    @Override
    public List<Book> findAll() {
        return null;
    }
}
