package org.nikitactr.bookservice.service;

import lombok.extern.log4j.Log4j2;
import org.nikitactr.bookservice.exception.BookNotFoundException;
import org.nikitactr.bookservice.exception.DuplicateIsbnException;
import org.nikitactr.bookservice.mapper.BookMapper;
import org.nikitactr.bookservice.model.Book;
import org.nikitactr.bookservice.payload.request.BookLoanRequest;
import org.nikitactr.bookservice.payload.request.BookRequest;
import org.nikitactr.bookservice.payload.response.BookResponse;
import org.nikitactr.bookservice.repository.BookRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Log4j2
public class BookService {

    private final RestTemplate restTemplate;

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    public BookService(RestTemplate restTemplate, BookRepository bookRepository, BookMapper bookMapper) {
        this.restTemplate = restTemplate;
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    public List<BookResponse> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        log.info("Retrieved all books");
        return bookMapper.booksToBookResponses(books);
    }

    public BookResponse findBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> {
                    String message = "Book with id: " + id + " not found";
                    log.error(message);
                    return new BookNotFoundException(message);
                });
        log.info("Retrieved book with id: {}", id);
        return bookMapper.bookToBookResponse(book);
    }

    public BookResponse findBookByISBN(String isbn) {
        Book book = bookRepository.findByIsbn(isbn).orElseThrow(() -> {
            String message = "Book with id: " + isbn + " not found";
            log.error(message);
            return new BookNotFoundException("Book with ISBN: " + isbn + " not found");
        });
        log.info("Retrieved book with ISBN: {}", isbn);
        return bookMapper.bookToBookResponse(book);
    }

    public void addBook(BookRequest bookRequest, String bearerToken) {
        String isbn = bookRequest.getIsbn();
        if (bookRepository.existsByIsbn(isbn)) {
            String message = "Book with ISBN: " + isbn + " already exists";
            log.error(message);
            throw new DuplicateIsbnException(message);
        }
        Book book = bookMapper.bookRequestToBook(bookRequest);
        bookRepository.save(book);
        log.info("Added book with id: {}", book.getId());

        sendBookLoanRequest(book.getId(), bearerToken);
    }

    public void updateBook(Long id, BookRequest bookRequest) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> {
                    String message = "Book with id: " + id + " not found";
                    log.error(message);
                    return new BookNotFoundException(message);
                });
        book.setIsbn(bookRequest.getIsbn());
        book.setTitle(bookRequest.getTitle());
        book.setGenre(bookRequest.getGenre());
        book.setDescription(bookRequest.getDescription());
        book.setAuthor(bookRequest.getAuthor());
        bookRepository.save(book);
        log.info("Updated book with id: {}", id);
    }


    @Transactional
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> {
                    String message = "Book with id: " + id + " not found";
                    log.error(message);
                    return new BookNotFoundException(message);
                });

        bookRepository.deleteBookAndLoans(book.getId());
        bookRepository.delete(book);
        log.info("Deleted book with id: {}", id);
    }

    private void sendBookLoanRequest(Long bookId, String bearerToken) {
        String libraryServiceUrl = "http://localhost:8082/library/loans";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", bearerToken);
        BookLoanRequest libraryBookRequest = new BookLoanRequest(bookId);
        HttpEntity<BookLoanRequest> requestEntity = new HttpEntity<>(libraryBookRequest, headers);
        restTemplate.postForObject(libraryServiceUrl, requestEntity, Void.class);
        log.info("Sent book loan request for book with id: {}", bookId);
    }
}