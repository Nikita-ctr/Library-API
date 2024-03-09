package org.nikitactr.libraryservice.service;

import org.nikitactr.libraryservice.model.Book;
import org.nikitactr.libraryservice.model.BookLoan;
import org.nikitactr.libraryservice.repository.BookLoanRepository;
import org.nikitactr.libraryservice.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LibraryService {
    private final BookLoanRepository bookLoanRepository;

    private final BookRepository bookRepository;

    public LibraryService(BookLoanRepository bookLoanRepository, BookRepository bookRepository) {
        this.bookLoanRepository = bookLoanRepository;
        this.bookRepository = bookRepository;
    }

    public void addBookLoan(Long bookId) {
        BookLoan bookLoan = new BookLoan();
        bookLoan.setBook(bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found")));
        bookLoan.setLoanTime(LocalDateTime.now());
        bookLoan.setReturnTime(LocalDateTime.now().plusHours(4));

        bookLoanRepository.save(bookLoan);
    }

    public List<Book> findAvailableBooks() {
        List<Book> allBooks = bookRepository.findAll();
        List<BookLoan> borrowedBooks = bookLoanRepository.findAll();

        List<Book> availableBooks = allBooks.stream()
                .filter(book -> borrowedBooks.stream()
                        .noneMatch(loan -> loan.getBook().getId().equals(book.getId())))
                .toList();

        return availableBooks;
    }
}