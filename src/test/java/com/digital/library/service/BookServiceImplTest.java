package com.digital.library.service;

import com.digital.library.data.ResourceType;
import com.digital.library.data.model.Book;
import com.digital.library.data.model.Category;
import com.digital.library.data.payloads.BookRequest;
import com.digital.library.data.payloads.MessageResponse;
import com.digital.library.data.payloads.SearchResponse;
import com.digital.library.data.repository.BookRepository;
import com.digital.library.exceptions.ApiException;
import com.digital.library.exceptions.ResourceNotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RunWith(SpringRunner.class)
public class BookServiceImplTest {

    @TestConfiguration
    static class BookServiceImplTestContextConfiguration {
        @Bean
        public BookService bookService() {
            return new BookServiceImpl();
        }
    }

    @Autowired
    BookService bookService;

    @MockBean
    BookRepository bookRepository;

    Book b1;
    Category cat;

    @Before
    public void setUp() {
        cat = new Category();
        cat.setId(1);
        cat.setName("Science");
        Set<Category> categorySet = new HashSet<>(Arrays.asList(cat));
        b1 = new Book();
        b1.setId(1);
        b1.setTitle("Title 1");
        b1.setAuthor("Author 1");
        b1.setCategorySet(categorySet);

        Mockito.when(bookRepository.findById(1))
                .thenReturn(java.util.Optional.of(b1));
    }

    @Test
    public void testUpdateOK() {
        BookRequest br = new BookRequest("Title 1", "Author 123", Arrays.asList(cat));
        b1.setAuthor("Changed author");
        Mockito.when(bookRepository.save(b1)).thenReturn(b1);

        try {
            Assert.assertEquals(
                    new MessageResponse("Book is updated.").getMessage(),
                    bookService.update(1, br).getMessage()
            );

        } catch (ResourceNotFoundException e) {
            Assert.fail(e.getMessage());
        } catch (ApiException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testFindAllOK() {
        Mockito.when(bookRepository.findAll()).thenReturn(Arrays.asList(b1));

        List<Book> b = bookService.findAll();
        Assert.assertNotNull(b);
        Assert.assertEquals(1, b.size());
        Assert.assertEquals("Incorrect book found!",b1,b.get(0));
    }

    @Test
    public void testFindByIdOK() {
        try {
            Book b = bookService.findById(1);
            Assert.assertEquals("Incorrect book found!",b1,b);
        } catch (ResourceNotFoundException e) {
            Assert.fail("ResourceNotFoundException: "+e.getMessage());
        }
    }

    @Test
    public void testFindById_NotFound() {
        try {
            Book b = bookService.findById(10);
            Assert.fail("Should return exception");
        } catch (ResourceNotFoundException e) {
           Assert.assertEquals(new ResourceNotFoundException(ResourceType.BOOK.toString(),"bookId","10").getMessage(), e.getMessage());
        }
    }

    public void testSearchOK() {
        SearchResponse rsp = SearchResponse.builder()
                .books(Arrays.asList(b1))
                .numberOfPages(1).numberOfResults(1).build();

        Assert.assertEquals(bookService.search("1", "author", 0, 1, "ASC"), rsp);
    }

}
