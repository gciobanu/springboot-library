package com.digital.library.controller;

import com.digital.library.data.model.Book;
import com.digital.library.data.payloads.BookRequest;
import com.digital.library.data.payloads.MessageResponse;
import com.digital.library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/library/book")
public class BookController {
    @Autowired
    BookService bookService;

    @GetMapping("/all")
    public ResponseEntity<List<Book>> getAllBooks() {
        return new ResponseEntity<>(this.bookService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable("id") Integer bookId) {
        return new ResponseEntity<>(this.bookService.findById(bookId), HttpStatus.OK);
    }

    //TODO find book by category
    //TODO find book by title and author

    @PostMapping("/new")
    public ResponseEntity<MessageResponse> addBook(@RequestBody BookRequest book) {
        MessageResponse response = this.bookService.save(book);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> updateBook( @PathVariable Integer id, @RequestBody BookRequest book) {
        MessageResponse response = this.bookService.update(id, book);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable("id") Integer id) {
        this.bookService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
