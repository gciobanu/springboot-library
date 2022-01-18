package com.digital.library.service;

import com.digital.library.data.ResourceType;
import com.digital.library.data.model.Book;
import com.digital.library.data.payloads.BookRequest;
import com.digital.library.data.payloads.MessageResponse;
import com.digital.library.data.payloads.SearchResponse;
import com.digital.library.data.repository.BookRepository;
import com.digital.library.exceptions.ApiException;
import com.digital.library.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BookServiceImpl implements BookService {
    @Autowired
    BookRepository bookRepository;

    @Override
    public Book save(BookRequest book) throws ApiException {
        try {
            Book newBook = new Book();
            newBook.setAuthor(book.getAuthor());
            newBook.setTitle(book.getTitle());
            newBook.setCategorySet(new HashSet<>(book.getCategories()));

            return bookRepository.save(newBook);
        } catch (Exception e) {
            throw new ApiException("Error saving the book: " + book.getTitle());
        }

    }

    @Override
    public MessageResponse update(Integer bookId, BookRequest book) throws  ResourceNotFoundException, ApiException {
        Optional<Book> existing = bookRepository.findById(bookId);
        if (existing.isEmpty()) {
            throw new ResourceNotFoundException(ResourceType.BOOK.toString(),"bookId",bookId.toString());
        }

        Book toChange = existing.get();
        toChange.setCategorySet(new HashSet<>(book.getCategories()));
        toChange.setAuthor(book.getAuthor());
        toChange.setTitle(book.getTitle());

        try {
            if (this.bookRepository.save(toChange) != null) {
                return new MessageResponse("Book is updated.");
            } else {
                throw new ApiException("Error retrieving the updated book: " + book.getTitle());
            }
        } catch (Exception e) {
            throw new ApiException("Error saving the book: " + book.getTitle());
        }
    }

    @Override
    public void delete(Integer bookId) throws ApiException {
        try {
            this.bookRepository.deleteById(bookId);
        } catch (Exception e) {
            throw new ApiException("Error removing the book.");
        }
    }

    @Override
    public Book findById(Integer bookId) throws ResourceNotFoundException {
        Optional<Book> bookO = this.bookRepository.findById(bookId);

        if(bookO.isEmpty()) {
            throw new ResourceNotFoundException(ResourceType.BOOK.toString(),"bookId",bookId.toString());
        }

        return bookO.get();
    }

    @Override
    public SearchResponse search(String title, String author, Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());

        Page<Book> pagedResult = this.bookRepository.findAll(Specification.where(hasAuthor(author)).and(titleContains(title)), paging);

        SearchResponse.SearchResponseBuilder response = SearchResponse.builder();

        if(pagedResult.hasContent()) {
            response.books(pagedResult.getContent())
                    .numberOfPages(pagedResult.getTotalPages())
                    .numberOfResults(pagedResult.getNumberOfElements());


        } else {
            response.books(new ArrayList<Book>())
                    .numberOfPages(0)
                    .numberOfResults(0);
        }

        return response.build();
    }

    @Override
    public List<Book> findAll() {
        return this.bookRepository.findAll();
    }

    static Specification<Book> hasAuthor(String author) {
        return (book, cq, cb) -> cb.like(cb.lower(book.get("author")), "%" + author.toLowerCase()+ "%");
    }

    static Specification<Book> titleContains(String title) {
        return (book, cq, cb) -> cb.like(cb.lower(book.get("title")), "%" + title.toLowerCase() + "%");
    }
}
