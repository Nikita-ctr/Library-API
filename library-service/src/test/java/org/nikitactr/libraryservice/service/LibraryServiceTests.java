package org.nikitactr.libraryservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.nikitactr.libraryservice.exception.BookNotFoundException;
import org.nikitactr.libraryservice.mapper.BookMapper;
import org.nikitactr.libraryservice.model.Book;
import org.nikitactr.libraryservice.repository.BookLoanRepository;
import org.nikitactr.libraryservice.repository.BookRepository;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class LibraryServiceTests {


    private LibraryService libraryService;

    @Mock
    private BookRepository bookRepository;

    @Mock
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
        book.setIsbn("1234567890");
        bookRepository.deleteById(book.getId());
        assertThrows(BookNotFoundException.class, () -> {
            libraryService.addBookLoan(book.getId());
        });
    }
}