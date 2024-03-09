package org.nikitactr.libraryservice.service;

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
        BookLoan bookLoan = new BookLoan();
        bookLoan.setBook(bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found")));
        bookLoan.setLoanTime(LocalDateTime.now());
        bookLoan.setReturnTime(LocalDateTime.now().plusHours(4));
        bookLoanRepository.save(bookLoan);
    }

    public List<BookResponse> findAvailableBooks() {
        List<Book> allBooks = bookRepository.findAll();
        List<BookLoan> borrowedBooks = bookLoanRepository.findAll();
        bookMapper.booksToBookResponses(allBooks);
        List<Book> filteredBooks = allBooks.stream()
                .filter(book -> borrowedBooks.stream()
                        .noneMatch(loan -> loan.getBook().getId().equals(book.getId())))
                .collect(Collectors.toList());

        return bookMapper.booksToBookResponses(filteredBooks);
    }
}