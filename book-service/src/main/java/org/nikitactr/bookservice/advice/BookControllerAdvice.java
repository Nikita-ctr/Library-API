package org.nikitactr.bookservice.advice;

import org.nikitactr.bookservice.exception.BookNotFoundException;
import org.nikitactr.bookservice.exception.DuplicateIsbnException;
import org.nikitactr.bookservice.exception.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
public class BookControllerAdvice {

    @ExceptionHandler(BookNotFoundException.class)
    public ErrorMessage handleBookNotFoundException(BookNotFoundException ex, WebRequest request) {
        return new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false));
    }

    @ExceptionHandler(DuplicateIsbnException.class)
    public ErrorMessage handleDuplicateIsbnException(DuplicateIsbnException ex, WebRequest request) {
        return new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false));
    }
}
