package com.digital.library.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.digital.library.data.model.Book;
import com.digital.library.data.model.Category;
import com.digital.library.data.payloads.BookRequest;
import com.digital.library.data.payloads.MessageResponse;
import com.digital.library.data.payloads.SearchResponse;
import com.digital.library.exceptions.ApiException;
import com.digital.library.exceptions.ResourceNotFoundException;
import com.digital.library.service.BookService;
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
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RunWith(SpringRunner.class)
@WebMvcTest(BookController.class)
public class BookControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService service;

    @Test
    public void givenBooks_whenGetAll_thenReturnJsonArray()
            throws Exception {

        Book book = new Book();
        book.setTitle("Title 1");

        List<Book> allBooks = Arrays.asList(book);

        given(service.findAll()).willReturn(allBooks);

        mvc.perform(get("/library/book/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is(book.getTitle())));
    }

    @Test
    public void givenBooks_whenGetById_thenReturnJsonBook()
            throws Exception {

        Book book = new Book();

        try {
            given(service.findById(1)).willReturn(book);
        } catch (ResourceNotFoundException e) {
            Assert.fail();
        }

        mvc.perform(get("/library/book/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("title", is(book.getTitle())));
    }

    @Test
    public void search_returnJsonArray_OK()
            throws Exception {

        Book book = new Book();
        book.setTitle("Title 1");
        book.setAuthor("Author 1");
        List<Book> allBooks = Arrays.asList(book);

        SearchResponse rsp = SearchResponse.builder()
                .books(allBooks).numberOfPages(1).numberOfResults(1)
                .build();

        given(service.search(
                "1",
                "Author",
                0,
                5,
                "title"
        )).willReturn(rsp);

        mvc.perform(get("/library/book/search?title=1&author=Author&pageNo=0&pageSize=5&sortBy=title")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("books[0].title", is(book.getTitle())))
                .andExpect(jsonPath("numberOfResults", is(1)))
                .andExpect(jsonPath("numberOfPages", is(1)));
    }

    @Test
    public void newBook_OK() {

        Book book = new Book();
        book.setTitle("Title 1");
        book.setAuthor("Author");
        Category cat = new Category();
        cat.setId(1);
        cat.setName("Science");
        book.setCategorySet(Stream.of(cat).collect(Collectors.toSet()));

        BookRequest req = new BookRequest();
        req.setTitle(book.getTitle());
        req.setAuthor(book.getAuthor());
        req.setCategories(Stream.of(cat).collect(Collectors.toList()));

        try {
            given(service.save(req)).willReturn(book);
            ObjectMapper objectMapper = new ObjectMapper();

            mvc.perform(post("/library/book/new")
                            .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(req)))
                    .andExpect(status().isCreated());
        } catch (ApiException e) {
            e.printStackTrace();
            Assert.fail();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            Assert.fail();
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }


    }


    @Test
    public void updateBook_OK()
            throws Exception, ApiException {

        Book book = new Book();
        book.setTitle("Title 1");
        book.setAuthor("Author");
        Category cat = new Category();
        cat.setId(1);
        cat.setName("Science");
        book.setCategorySet(Stream.of(cat).collect(Collectors.toSet()));

        BookRequest req = new BookRequest();
        req.setTitle(book.getTitle());
        req.setAuthor(book.getAuthor());
        req.setCategories(Stream.of(cat).collect(Collectors.toList()));

        MessageResponse rsp = new MessageResponse("Book is updated.");

        try {
            given(service.update(1,req)).willReturn(rsp);
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
            Assert.fail();
        }

        ObjectMapper objectMapper = new ObjectMapper();

        mvc.perform(put("/library/book/1")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteBook_OK() {

        MessageResponse rsp = new MessageResponse("Book is removed.");

        try {
            given(service.delete(1)).willReturn(rsp);
            ObjectMapper objectMapper = new ObjectMapper();
            mvc.perform(delete("/library/book/1"))
                    .andExpect(status().isOk());
        } catch (ApiException e) {
            e.printStackTrace();
            Assert.fail();
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }
}
