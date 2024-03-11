package org.nikitactr.bookservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nikitactr.bookservice.exception.BookNotFoundException;
import org.nikitactr.bookservice.model.Book;
import org.nikitactr.bookservice.payload.request.BookRequest;
import org.nikitactr.bookservice.payload.response.BookResponse;
import org.nikitactr.bookservice.repository.BookRepository;
import org.nikitactr.bookservice.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@ActiveProfiles("test")
public class BookServiceTest {
    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    public  void setUp() {
        bookRepository.deleteAll();
    }

    @Test
    public void testGetAllBooks() {

        Book book1 = new Book("123", "Book 1", "Genre 1", "Description 1", "Author 1");
        Book book2 = new Book("321", "Book 2", "Genre 2", "Description 2", "Author 2");
        bookRepository.saveAll(Arrays.asList(book1, book2));

        List<BookResponse> books = bookService.getAllBooks();

        assertEquals(2, books.size());
        assertEquals("Book 1", books.get(0).getTitle());
        assertEquals("Book 2", books.get(1).getTitle());


    }

    @Test
    public void testFindBookById() {
        Book book = new Book("ISBN1", "Book 1", "Genre 1", "Description 1", "Author 1");
        bookRepository.save(book);

        BookResponse bookResponse = bookService.findBookById(book.getId());

        assertEquals("Book 1", bookResponse.getTitle());
        assertEquals("ISBN1", bookResponse.getIsbn());
    }

    @Test
    public void testFindBookByISBN() {
        Book book = new Book("ISBN1", "Book 1", "Genre 1", "Description 1", "Author 1");
        bookRepository.save(book);

        BookResponse bookResponse = bookService.findBookByISBN("ISBN1");

        assertEquals("Book 1", bookResponse.getTitle());
        assertEquals("ISBN1", bookResponse.getIsbn());
    }

    @Test
    public void testUpdateBook() {
        Book book = new Book("ISBN1", "Book 1", "Genre 1", "Description 1", "Author 1");
        bookRepository.save(book);

        BookRequest bookRequest = new BookRequest("ISBN2", "Book 2", "Genre 2", "Description 2", "Author 2");

        bookService.updateBook("ISBN1", bookRequest);

        Book updatedBook = bookRepository.findByIsbn("ISBN2")
                .orElseThrow(() -> new BookNotFoundException("Book not found"));
        assertEquals("Book 2", updatedBook.getTitle());
        assertEquals("Genre 2", updatedBook.getGenre());
    }

    @Test
    public void testDeleteBook() {
        Book book = new Book(1L,"ISBN1", "Book 1", "Genre 1", "Description 1", "Author 1");
        bookRepository.save(book);

        bookRepository.deleteAll();
        assertFalse(bookRepository.findByIsbn("ISBN1").isPresent());
    }
}