package org.nikitactr.libraryservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.MockitoAnnotations;
import org.nikitactr.libraryservice.mapper.BookMapper;
import org.nikitactr.libraryservice.model.Book;
import org.nikitactr.libraryservice.model.BookLoan;
import org.nikitactr.libraryservice.payload.reponse.BookResponse;
import org.nikitactr.libraryservice.payload.reponse.BorrowedBookResponse;
import org.nikitactr.libraryservice.repository.BookLoanRepository;
import org.nikitactr.libraryservice.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class LibraryServiceTests {

    private LibraryService libraryService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookLoanRepository bookLoanRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        BookMapper bookMapper = Mappers.getMapper(BookMapper.class);
        libraryService = new LibraryService(bookLoanRepository, bookRepository, bookMapper);
    }

    @Test
    public void testAddBookLoan() {
        Book book = new Book();
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setGenre("Test Genre");
        book.setDescription("Test Description");
        book.setIsbn("1234567890");
        bookRepository.save(book);

        libraryService.addBookLoan(book.getId());

        List<BookLoan> bookLoans = bookLoanRepository.findAll();
        assertEquals(1, bookLoans.size());
        BookLoan bookLoan = bookLoans.get(0);
        assertEquals(book.getId(), bookLoan.getBook().getId());

        bookLoanRepository.deleteAll();
        bookRepository.deleteAll();
    }

    @Test
    public void testFindAvailableBooks() {
        Book book1 = new Book();
        book1.setTitle("Book 1");
        book1.setAuthor("Author 1");
        book1.setGenre("Genre 1");
        book1.setDescription("Description 1");
        book1.setIsbn("1111111111");
        bookRepository.save(book1);

        Book book2 = new Book();
        book2.setTitle("Book 2");
        book2.setAuthor("Author 2");
        book2.setGenre("Genre 2");
        book2.setDescription("Description 2");
        book2.setIsbn("2222222222");
        bookRepository.save(book2);

        List<BookResponse> availableBooks = libraryService.findAvailableBooks();

        assertEquals(2, availableBooks.size());
        assertEquals(book1.getTitle(), availableBooks.get(0).getTitle());
        assertEquals(book2.getTitle(), availableBooks.get(1).getTitle());

    }


    @Test
    public void testFindNotAvailableBooks() {

        Book book1 = new Book();
        book1.setTitle("Book 1");
        book1.setAuthor("Author 1");
        book1.setGenre("Genre 1");
        book1.setDescription("Description 1");
        book1.setIsbn("1111111111");
        bookRepository.save(book1);

        Book book2 = new Book();
        book2.setTitle("Book 2");
        book2.setAuthor("Author 2");
        book2.setGenre("Genre 2");
        book2.setDescription("Description 2");
        book2.setIsbn("2222222222");
        bookRepository.save(book2);

        BookLoan bookLoan = new BookLoan();
        bookLoan.setBook(book1);
        bookLoan.setLoanTime(LocalDateTime.now());
        bookLoan.setReturnTime(LocalDateTime.now().plusHours(4));
        bookLoanRepository.save(bookLoan);

        List<BorrowedBookResponse> notAvailableBooks = libraryService.findNotAvailableBooks();

        assertEquals(1, notAvailableBooks.size());
        assertEquals(book1.getTitle(), notAvailableBooks.get(0).getTitle());

        bookLoanRepository.deleteAll();
        bookRepository.deleteAll();
    }
}