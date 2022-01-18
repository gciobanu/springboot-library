package com.digital.library.service;

import com.digital.library.data.ResourceType;
import com.digital.library.data.model.Book;
import com.digital.library.data.model.Loan;
import com.digital.library.data.model.UserMember;
import com.digital.library.data.payloads.LoanRequest;
import com.digital.library.data.payloads.ReturnLoanRequest;
import com.digital.library.data.repository.BookRepository;
import com.digital.library.data.repository.LoanRepository;
import com.digital.library.data.repository.UserMemberRepository;
import com.digital.library.exceptions.ApiException;
import com.digital.library.exceptions.OutstandingBookException;
import com.digital.library.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LoanServiceImpl implements LoanService {
    @Autowired
    LoanRepository loanRepository;
    @Autowired
    UserMemberRepository userMemberRepository;
    @Autowired
    BookRepository bookRepository;

    private static final int MAX_BOOKS_NO_IN_LOAN = 3;
    private static final int MAX_DAYS_ALLOWED_FOR_LOAN = 30; //just an example

    @Override
    public void loanBook(LoanRequest loanRequest) throws OutstandingBookException, ResourceNotFoundException, ApiException {
        UserMember userMember = this.getUserMemberById(loanRequest.getUserMember().getId());

        if (!this.isOutstandingLoanForUserMember(userMember)) {
            List<Book> books = loanRequest.getBooks();

            for (Book book: books) {
                // does book exists ?
                // maybe use a code (to add in entity) for identifying the book here instead of ID?
                Book existingBook = this.checkBookExistsById(book.getId());

                Loan newBookLoan = new Loan();
                newBookLoan.setBook(existingBook);
                newBookLoan.setUsermember(userMember);
                newBookLoan.setLoanDate(LocalDate.now());

                try {
                    loanRepository.save(newBookLoan);
                } catch (Exception e) {
                    //e.printStackTrace();
                    throw new ApiException("Error loaning the book with ID: " + book.getId());
                }

            }
        }
    }

    @Override
    public void returnBook(ReturnLoanRequest loanRequest) throws ResourceNotFoundException, ApiException {
        UserMember userMember = this.getUserMemberById(loanRequest.getUserMember().getId());

        List<Loan> outstandingLoan = loanRepository.findByUsermemberAndReturnDateIsNull(userMember);
        List<Loan> returnLoans = new ArrayList<>();

        for (Loan loan : outstandingLoan) {
            for (Book book : loanRequest.getBooks()) {
                if (loan.getBook().getId() == book.getId()) {
                    loan.setReturnDate(LocalDate.now());
                    returnLoans.add(loan);
                }
            }
        }

        // books for return may not be valid (no corresponding "open" loan)
        if (returnLoans.isEmpty()) {
            throw new ResourceNotFoundException(
                    ResourceType.BOOK.toString(),
                    "id",
                    loanRequest.getBooks().stream().map(b -> b.getId()).collect(Collectors.toList())
                            .toString());
        }

        try {
            loanRepository.saveAll(returnLoans);
        } catch (Exception e) {
            throw new ApiException("Error on books return!");
        }
    }

    /**
     * Check if user member exists and if so return his details
     *
     * @param userId
     * @return UserMember
     * @throws ResourceNotFoundException
     */
    private UserMember getUserMemberById(Integer userId) throws ResourceNotFoundException {
        // does user ID exists ?
        Optional<UserMember> existingUserO = userMemberRepository.findById(userId);
        if (existingUserO.isEmpty()) {
            throw new ResourceNotFoundException(
                    ResourceType.USER.toString(),
                    "id",
                    userId.toString());
        }

        return existingUserO.get();
    }

    /**
     * Check for user member if any outstanding loan or if number of books reached the maximum allowed for loaning
     *
     * @param userMember
     * @return boolean false if no outstanding loan or if number of maximum allowed books is not reached
     * @throws OutstandingBookException
     */
    private boolean isOutstandingLoanForUserMember(UserMember userMember) throws OutstandingBookException {
        // check is valid for new book loan ?
        List<Loan> outstandingLoan = loanRepository.findByUsermemberAndReturnDateIsNull(userMember);

        if (outstandingLoan == null || outstandingLoan.isEmpty()) {
            return false;
        }

        if (outstandingLoan != null && outstandingLoan.size() >= MAX_BOOKS_NO_IN_LOAN) {
            //TODO new exception type for this case  ?
            throw new OutstandingBookException("No more than " + MAX_BOOKS_NO_IN_LOAN + " books are allowed!");
        }

        for (Loan loan : outstandingLoan) {
            // check each loan date if it has more than 30 days + 1 passed (+1 to allow returning and new loan on the 30th day)
            // if any outstanding book after 30 days, do not allow new loan
            if (loan != null && loan.getLoanDate() != null &&
                    loan.getLoanDate().plusDays(MAX_DAYS_ALLOWED_FOR_LOAN).isBefore(LocalDate.now().plusDays(1))) {
                throw new OutstandingBookException("This user member has outstanding books to return!");
            }
        }
        return false;
    }

    /**
     * Check if book member exists and if so return its details
     *
     * @param bookId
     * @return Book
     * @throws ResourceNotFoundException
     */
    private Book checkBookExistsById(Integer bookId) throws ResourceNotFoundException, OutstandingBookException {
        if (bookId == null) {
            throw new ResourceNotFoundException( //todo new custom bad request exception?
                    ResourceType.BOOK.toString(),
                    "id",
                    "");
        }
        // does book exists ?
        Optional<Book> existingO = bookRepository.findById(bookId);
        if (existingO.isEmpty()) {
            throw new ResourceNotFoundException(
                    ResourceType.BOOK.toString(),
                    "id",
                    bookId.toString());
        }

        // check if book is still under loan and not yet return
        List<Loan> loanedBook = this.loanRepository.findByBookId(bookId);
        if (loanedBook != null && !loanedBook.isEmpty()) {
            throw new OutstandingBookException("Book "+bookId+" is not yet returned!");
        }

        return existingO.get();
    }
}
