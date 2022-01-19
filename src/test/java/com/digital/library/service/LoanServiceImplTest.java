package com.digital.library.service;

import com.digital.library.data.ResourceType;
import com.digital.library.data.model.Book;
import com.digital.library.data.model.Category;
import com.digital.library.data.model.Loan;
import com.digital.library.data.model.UserMember;
import com.digital.library.data.payloads.LoanRequest;
import com.digital.library.data.repository.BookRepository;
import com.digital.library.data.repository.LoanRepository;
import com.digital.library.data.repository.UserMemberRepository;
import com.digital.library.exceptions.ApiException;
import com.digital.library.exceptions.OutstandingBookException;
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

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RunWith(SpringRunner.class)
public class LoanServiceImplTest {
    @TestConfiguration
    static class LoanServiceImplTestContextConfiguration {
        @Bean
        public LoanService loanService() {
            return new LoanServiceImpl();
        }
    }

    @Autowired
    LoanService loanService;

    @MockBean
    BookRepository bookRepository;

    @MockBean
    LoanRepository loanRepository;

    @MockBean
    UserMemberRepository userMemberRepository;

    Book b1;
    Category cat;
    UserMember user1;

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

        Mockito.when(bookRepository.findById(1)).thenReturn(java.util.Optional.of(b1));

        user1 = new UserMember();
        user1.setId(1);
        user1.setFirstname("Joe");
        user1.setSurname("Doe");

        Mockito.when(userMemberRepository.findById(1)).thenReturn(java.util.Optional.ofNullable(user1));


    }

    @Test
    public void loanOneBook_OK() {
        LoanRequest lr = new LoanRequest();
        lr.setBooks(Arrays.asList(b1));
        lr.setUserMember(user1);

        // no current loan for this book and user
        Mockito.when(loanRepository.findByBookId(1)).thenReturn(null);
        Mockito.when(loanRepository.findByUsermemberAndReturnDateIsNull(user1)).thenReturn(null);
        Mockito.when(loanRepository.save(Mockito.any(Loan.class))).thenReturn(new Loan());

        try {
            loanService.loanBook(lr);
        } catch (OutstandingBookException e) {
            e.printStackTrace();
            Assert.fail();
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
            Assert.fail();
        } catch (ApiException e) {
            e.printStackTrace();
            Assert.fail();
        }

        Mockito.verify(loanRepository, Mockito.times(1)).save(Mockito.any(Loan.class));
    }

    @Test
    public void loanThreeBook_OK() {
        LoanRequest lr = new LoanRequest();
        List<Book> books = new ArrayList<>();
        Book b_1 = addBookToList(1, books);
        Book b_2 = addBookToList(2,  books);
        Book b_3 = addBookToList(3, books);

        lr.setBooks(books);
        lr.setUserMember(user1);

        Mockito.when(bookRepository.findById(2)).thenReturn(Optional.of(b_2));
        Mockito.when(bookRepository.findById(3)).thenReturn(Optional.of(b_3));

        // no current loan for this book and user
        Mockito.when(loanRepository.findByBookId(Mockito.any())).thenReturn(null);
        Mockito.when(loanRepository.findByUsermemberAndReturnDateIsNull(user1)).thenReturn(null);
        Mockito.when(loanRepository.save(Mockito.any(Loan.class))).thenReturn(new Loan());

        try {
            loanService.loanBook(lr);
        } catch (OutstandingBookException e) {
            e.printStackTrace();
            Assert.fail();
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
            Assert.fail();
        } catch (ApiException e) {
            e.printStackTrace();
            Assert.fail();
        }

        Mockito.verify(loanRepository, Mockito.times(3)).save(Mockito.any(Loan.class));
    }

    @Test
    public void loanFourthBook_Exception() {
        LoanRequest lr = new LoanRequest();
        List<Book> books = new ArrayList<>();
        Book b_4 = addBookToList(4, books);

        lr.setBooks(books);
        lr.setUserMember(user1);

        Mockito.when(bookRepository.findById(4)).thenReturn(Optional.of(b_4));

        // no current loan for this book and user
        Mockito.when(loanRepository.findByBookId(4)).thenReturn(null);

        //mock existing loans for user
        List<Loan> currentLoans = new ArrayList<>();
        currentLoans.add(new Loan());
        currentLoans.add(new Loan());
        currentLoans.add(new Loan());
        Mockito.when(loanRepository.findByUsermemberAndReturnDateIsNull(user1)).thenReturn(currentLoans);

        try {
            loanService.loanBook(lr);
        } catch (OutstandingBookException e) {
            Assert.assertEquals(
                    "No more than " + LoanServiceImpl.MAX_BOOKS_NO_IN_LOAN + " books are allowed!",
                    e.getMessage()
            );
        } catch (ResourceNotFoundException e) {
            Assert.fail(e.getMessage());
        } catch (ApiException e) {
            Assert.fail(e.getMessage());
        }

        Mockito.verify(loanRepository, Mockito.times(0)).save(Mockito.any());
    }

    @Test
    public void loanOutstandingBook_Exception() {
        LoanRequest lr = new LoanRequest();
        List<Book> books = new ArrayList<>();
        Book b_2 = addBookToList(2, books);

        lr.setBooks(books);
        lr.setUserMember(user1);

        Mockito.when(bookRepository.findById(2)).thenReturn(Optional.of(b_2));

        // no current loan for this book and user
        Mockito.when(loanRepository.findByBookId(2)).thenReturn(null);

        //mock existing outstanding loan for user
        List<Loan> currentLoans = new ArrayList<>();
        Loan loan = new Loan();
        loan.setReturnDate(null);
        loan.setLoanDate(LocalDate.now().minusDays(31));
        currentLoans.add(loan);
        Mockito.when(loanRepository.findByUsermemberAndReturnDateIsNull(user1)).thenReturn(currentLoans);

        try {
            loanService.loanBook(lr);
        } catch (OutstandingBookException e) {
            Assert.assertEquals(
                    "This user member has outstanding books to return!",
                    e.getMessage()
            );
        } catch (ResourceNotFoundException e) {
            Assert.fail(e.getMessage());
        } catch (ApiException e) {
            Assert.fail(e.getMessage());
        }

        Mockito.verify(loanRepository, Mockito.times(0)).save(Mockito.any());
    }

    @Test
    public void loanBookNotFound_Exception() {
        LoanRequest lr = new LoanRequest();
        List<Book> books = new ArrayList<>();
        Book b_2 = addBookToList(2, books);

        lr.setBooks(books);
        lr.setUserMember(user1);

        Mockito.when(bookRepository.findById(2)).thenReturn(null);

        // no current loan for this book and user
        Mockito.when(loanRepository.findByBookId(2)).thenReturn(null);

        try {
            loanService.loanBook(lr);
        } catch (OutstandingBookException e) {
            Assert.fail(e.getMessage());
        } catch (ResourceNotFoundException e) {
            Assert.assertEquals(
                    "Not Found the resource " + ResourceType.BOOK.name() +
                            " with property:id = 2",
                    e.getMessage()
            );
        } catch (ApiException e) {
            Assert.fail(e.getMessage());
        }

        Mockito.verify(loanRepository, Mockito.times(0)).save(Mockito.any());
    }

    @Test
    public void loanSameBookWithoutReturn_Error() {
        LoanRequest lr = new LoanRequest();
        List<Book> books = new ArrayList<>();
        books.add(b1);
        lr.setBooks(books);
        lr.setUserMember(user1);

        Mockito.when(bookRepository.findById(b1.getId())).thenReturn(Optional.ofNullable(b1));

        List<Loan> currentLoans = new ArrayList<>();
        Loan loan = new Loan();
        loan.setReturnDate(null);
        loan.setLoanDate(LocalDate.now().minusDays(1));
        loan.setBook(b1);
        currentLoans.add(loan);
        Mockito.when(loanRepository.findByBookId(1)).thenReturn(currentLoans);

        try {
            loanService.loanBook(lr);
        } catch (OutstandingBookException e) {
            Assert.assertEquals(
                    "Book 1 is not yet returned!",
                    e.getMessage()
            );
        } catch (ResourceNotFoundException e) {
            Assert.fail(e.getMessage());
        } catch (ApiException e) {
            Assert.fail(e.getMessage());
        }

        Mockito.verify(loanRepository, Mockito.times(0)).save(Mockito.any());
    }

    @Test
    public void loanSameBookToManyUsersNow_Error() {
        LoanRequest lr = new LoanRequest();
        List<Book> books = new ArrayList<>();
        books.add(b1);

        lr.setBooks(books);
        UserMember user2 = new UserMember();
        user2.setId(2);
        user2.setFirstname("Jane");
        user2.setSurname("Doe");
        lr.setUserMember(user2);

        Mockito.when(userMemberRepository.findById(2)).thenReturn(java.util.Optional.ofNullable(user2));

        Mockito.when(bookRepository.findById(b1.getId())).thenReturn(Optional.of(b1));

        //mock existing  loan for the book
        List<Loan> currentLoans = new ArrayList<>();
        Loan loan = new Loan();
        loan.setReturnDate(null);
        loan.setLoanDate(LocalDate.now().minusDays(1));
        loan.setUsermember(user1);
        currentLoans.add(loan);
        Mockito.when(loanRepository.findByBookId(b1.getId())).thenReturn(currentLoans);

        try {
            loanService.loanBook(lr);
        } catch (OutstandingBookException e) {
            Assert.assertEquals(
                    "Book 1 is not yet returned!",
                    e.getMessage()
            );
        } catch (ResourceNotFoundException e) {
            Assert.fail(e.getMessage());
        } catch (ApiException e) {
            Assert.fail(e.getMessage());
        }

        Mockito.verify(loanRepository, Mockito.times(0)).save(Mockito.any());
    }

    @Test
    public void returnOneBook_OK() {

    }

    public void returnThreeBooks_OK() {

    }

    public void returnWrongBook_NotFoundError() {

    }

    public void returnWithUnkownUserMember_NotFoundError() {

    }

    private Book addBookToList(Integer id, List<Book> books) {
        if (books == null) {
            books = new ArrayList<Book>();
        }
        Book b = new Book();
        b.setAuthor("Author "+id);
        b.setTitle("Title "+id);
        b.setCategorySet(Stream.of(cat).collect(Collectors.toSet()));
        b.setId(id);

        books.add(b);

        return b;
    }
}
