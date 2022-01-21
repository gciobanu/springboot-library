package com.digital.library.controller;

import com.digital.library.data.model.Book;
import com.digital.library.data.payloads.BookRequest;
import com.digital.library.data.payloads.MessageResponse;
import com.digital.library.data.payloads.SearchResponse;
import com.digital.library.exceptions.ApiException;
import com.digital.library.exceptions.ResourceNotFoundException;
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
        try{
            return new ResponseEntity<>(this.bookService.findById(bookId), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // find book by title and/or author
    @GetMapping("/search")
    public ResponseEntity<SearchResponse> search(
            @RequestParam("title") String title,
            @RequestParam("author") String author,
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "title") String sortBy
    ) {
        SearchResponse results = bookService.search(title, author, pageNo, pageSize, sortBy);
        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    @PostMapping("/new")
    public ResponseEntity<Book> addBook(@RequestBody BookRequest book) {
        try {
            Book response = this.bookService.save(book);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (ApiException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> updateBook( @PathVariable Integer id, @RequestBody BookRequest book) {
        MessageResponse response = null;
        try {
            response = this.bookService.update(id, book);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            response = new MessageResponse(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (ApiException e) {
            response = new MessageResponse(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteBook(@PathVariable("id") Integer id) {
        try {
            MessageResponse response = this.bookService.delete(id);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ApiException e) {
            MessageResponse response = new MessageResponse(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
