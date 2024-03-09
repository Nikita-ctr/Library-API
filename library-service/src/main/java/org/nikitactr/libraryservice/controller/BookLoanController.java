package org.nikitactr.libraryservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.nikitactr.libraryservice.payload.reponse.BookResponse;
import org.nikitactr.libraryservice.payload.request.BookLoanRequest;
import org.nikitactr.libraryservice.service.LibraryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/library/loans")
public class BookLoanController {
    private final LibraryService libraryService;

    public BookLoanController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @Operation(summary = "Borrow a book")
    @PostMapping
    public void borrowBook(@RequestBody BookLoanRequest bookLoanRequest) {
        libraryService.addBookLoan(bookLoanRequest.getBookId());
    }

    @Operation(summary = "Get all the available books")
    @GetMapping
    public List<BookResponse> getAvailableBooks() {
        return libraryService.findAvailableBooks();
    }
}