package org.nikitactr.libraryservice.service;

import lombok.extern.log4j.Log4j2;
import org.nikitactr.libraryservice.exception.BookNotFoundException;
import org.nikitactr.libraryservice.mapper.BookMapper;
import org.nikitactr.libraryservice.model.Book;
import org.nikitactr.libraryservice.model.BookLoan;
import org.nikitactr.libraryservice.payload.reponse.BookResponse;
import org.nikitactr.libraryservice.repository.BookLoanRepository;
import org.nikitactr.libraryservice.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class LibraryService {
    private final BookLoanRepository bookLoanRepository;

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    public LibraryService(BookLoanRepository bookLoanRepository, BookRepository bookRepository, BookMapper bookMapper) {
        this.bookLoanRepository = bookLoanRepository;
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    public void addBookLoan(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> {
                    String message = "Book with id: " + bookId + " not found";
                    log.error(message);
                    return new BookNotFoundException("Book with id: " + bookId + " not found");
                });

        BookLoan bookLoan = BookLoan.builder()
                .book(book)
                .loanTime(LocalDateTime.now())
                .returnTime(LocalDateTime.now().plusHours(4))
                .build();

        bookLoanRepository.save(bookLoan);
        log.info("Added book loan for book with id: {}", bookId);
    }

    public List<BookResponse> findAvailableBooks() {
        List<Book> availableBooks = bookRepository.findAll().stream()
                .filter(this::isBookAvailable)
                .collect(Collectors.toList());

        return bookMapper.booksToBookResponses(availableBooks);
    }

    private boolean isBookAvailable(Book book) {
        return bookLoanRepository.findAll().stream()
                .noneMatch(loan -> loan.getBook().getId().equals(book.getId()));
    }
}