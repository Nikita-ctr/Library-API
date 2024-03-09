package org.nikitactr.libraryservice.repository;

import org.nikitactr.libraryservice.model.BookLoan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookLoanRepository extends JpaRepository<BookLoan, Long> {
}
