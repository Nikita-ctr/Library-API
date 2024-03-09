package org.nikitactr.bookservice.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookLoanRequest {

    private Long bookId;
}