package com.docsachdi.repositories;


import com.docsachdi.entities.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
    Page<Book> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
