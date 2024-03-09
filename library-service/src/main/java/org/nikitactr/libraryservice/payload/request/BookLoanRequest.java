package org.nikitactr.libraryservice.payload.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookLoanRequest {

    private Long bookId;

}