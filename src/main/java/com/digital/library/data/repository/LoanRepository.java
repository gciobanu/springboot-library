package com.digital.library.data.repository;

import com.digital.library.data.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRepository  extends JpaRepository<Loan, Long> {
}
