package org.nikitactr.libraryservice.payload.reponse;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BorrowedBookResponse {

    private String isbn;

    private String title;

    private String genre;

    private String description;

    private String author;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime loanTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime returnTime;
}
