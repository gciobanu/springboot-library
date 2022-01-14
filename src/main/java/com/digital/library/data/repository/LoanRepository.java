package com.digital.library.data.repository;

import com.digital.library.data.model.Loan;
import com.digital.library.data.model.UserMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository  extends JpaRepository<Loan, Long> {

    List<Loan> findByUsermemberAndReturnDateIsNull(UserMember userMember);
}
