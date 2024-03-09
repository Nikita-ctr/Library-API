package org.nikitactr.bookservice.repository;

import org.nikitactr.bookservice.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByIsbn(String isbn);

    @Modifying
    @Query(value = "DELETE FROM book_loans WHERE book_id = :bookId", nativeQuery = true)
    void deleteBookAndLoans(Long bookId);

    boolean existsByIsbn(String isbn);
}
