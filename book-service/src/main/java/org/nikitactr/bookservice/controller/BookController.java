package org.nikitactr.bookservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.nikitactr.bookservice.payload.request.BookRequest;
import org.nikitactr.bookservice.payload.response.BookResponse;
import org.nikitactr.bookservice.service.BookService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @Operation(summary = "Get all books")
    @GetMapping
    public List<BookResponse> getAllBooks() {
        return bookService.getAllBooks();
    }

    @Operation(summary = "Find book by ID")
    @GetMapping("/{id}")
    public BookResponse findBookById(@PathVariable Long id) {
        return bookService.findBookById(id);
    }

    @Operation(summary = "Find book by ISBN")
    @GetMapping("/isbn/{isbn}")
    public BookResponse findBookByISBN(@PathVariable String isbn) {
        return bookService.findBookByISBN(isbn);
    }

    @Operation(summary = "Create a new book")
    @PostMapping
    public void addBook(@RequestBody BookRequest bookRequest, @RequestHeader("Authorization") String bearerToken) {
        bookService.addBook(bookRequest, bearerToken);
    }

    @Operation(summary = "Update an existing book")
    @PutMapping("/{isbn}")
    public void updateBook(@PathVariable String isbn, @RequestBody BookRequest bookRequest) {
        bookService.updateBook(isbn, bookRequest);
    }

    @Operation(summary = "Delete a book by isbn")
    @DeleteMapping("/{isbn}")
    public void deleteBook(@PathVariable String isbn) {
        bookService.deleteBook(isbn);
    }
}