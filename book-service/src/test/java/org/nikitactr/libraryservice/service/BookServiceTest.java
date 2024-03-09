package org.nikitactr.libraryservice.service;

import exception.BookNotFoundException;
import mapper.BookMapper;
import model.Book;
import payload.request.BookRequest;
import payload.response.BookResponse;
import repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;
import service.BookService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class BookServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private BookRepository bookRepository;

    private BookMapper bookMapper;

    private BookService bookService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        bookMapper = Mappers.getMapper(BookMapper.class);
        bookService = new BookService(restTemplate, bookRepository, bookMapper);
    }

    @Test
    public void testGetAllBooks() {
        Book book1 = new Book(1L, "1234567890", "Book 1", "Genre 1", "Description 1", "Author 1");
        Book book2 = new Book(2L, "0987654321", "Book 2", "Genre 2", "Description 2", "Author 2");
        List<Book> books = Arrays.asList(book1, book2);

        when(bookRepository.findAll()).thenReturn(books);
        List<BookResponse> bookResponses = bookService.getAllBooks();

        assertEquals(2, bookResponses.size());
        assertEquals("Book 1", bookResponses.get(0).getTitle());
        assertEquals("Book 2", bookResponses.get(1).getTitle());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    public void testFindBookById() {
        Long bookId = 1L;
        Book book = new Book(bookId, "123", "Book 1", "Genre 1", "Description 1", "Author 1");

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        BookResponse bookResponse = bookService.findBookById(bookId);

        assertNotNull(bookResponse);
        assertEquals("Book 1", bookResponse.getTitle());
        verify(bookRepository, times(1)).findById(bookId);
    }

    @Test
    public void testFindBookById_BookNotFound() {
        Long bookId = 1L;

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.findBookById(bookId));
        verify(bookRepository, times(1)).findById(bookId);
    }

    @Test
    public void testFindBookByISBN() {
        String isbn = "1234";
        Book book = new Book(1L, isbn, "Book 1", "Genre 1", "Description 1", "Author 1");

        when(bookRepository.findByIsbn(isbn)).thenReturn(book);

        BookResponse bookResponse = bookService.findBookByISBN(isbn);

        assertNotNull(bookResponse);
        assertEquals("Book 1", bookResponse.getTitle());
        verify(bookRepository, times(1)).findByIsbn(isbn);
    }

    @Test
    public void testAddBook() {

        BookRequest bookRequest = new BookRequest("12345", "Book 1", "Genre 1", "Description 1", "Author 1");
        Book book = bookMapper.bookRequestToBook(bookRequest);

        when(bookRepository.save(any(Book.class))).thenReturn(book);
        assertDoesNotThrow(() -> bookService.addBook(bookRequest));
    }

    @Test
    public void testUpdateBook() {
        Long bookId = 1L;
        BookRequest bookRequest = new BookRequest("123456", "Updated Book", "Updated Genre", "Updated Description", "Updated Author");
        Book book = new Book(bookId, "1234567890", "Book 1", "Genre 1", "Description 1", "Author 1");

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        assertDoesNotThrow(() -> bookService.updateBook(bookId, bookRequest));

        verify(bookRepository, times(1)).findById(bookId);
        verify(bookRepository, times(1)).save(book);
        assertEquals("Updated Book", book.getTitle());
        assertEquals("Updated Genre", book.getGenre());
        assertEquals("Updated Description", book.getDescription());
        assertEquals("Updated Author", book.getAuthor());
    }

    @Test
    public void testUpdateBook_BookNotFound() {
        Long bookId = 1L;
        BookRequest bookRequest = new BookRequest("1234567", "Updated Book", "Updated Genre", "Updated Description", "Updated Author");

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> bookService.updateBook(bookId, bookRequest));
        verify(bookRepository, times(1)).findById(bookId);
    }

    @Test
    public void testDeleteBook() {
        Long bookId = 1L;
        Book book = new Book(bookId, "12345678", "Book 1", "Genre 1", "Description 1", "Author 1");

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        assertDoesNotThrow(() -> bookService.deleteBook(bookId));

        verify(bookRepository, times(1)).findById(bookId);
        verify(bookRepository, times(1)).delete(book);
    }

    @Test
    public void testDeleteBook_BookNotFound() {
        Long bookId = 1L;

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.deleteBook(bookId));
        verify(bookRepository, times(1)).findById(bookId);
    }
}