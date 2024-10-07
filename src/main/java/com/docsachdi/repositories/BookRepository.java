package com.docsachdi.repositories;


import com.docsachdi.entities.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    Page<Book> findAllByOrderByCreatedAtDesc(Pageable pageable);

    List<Book> findByTitleContainingOrAuthorsNameContainingOrTagsNameContaining(String title, String author, String tag);
}
