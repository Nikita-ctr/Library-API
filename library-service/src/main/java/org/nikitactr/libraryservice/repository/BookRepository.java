package org.nikitactr.libraryservice.repository;

import org.nikitactr.libraryservice.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
