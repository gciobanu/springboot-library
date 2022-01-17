package com.digital.library.data.repository;

import com.digital.library.data.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    Page<Book> findByTitleOrAuthorContains(String title, String author, Pageable pageable);
}
