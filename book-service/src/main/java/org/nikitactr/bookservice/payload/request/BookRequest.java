package org.nikitactr.bookservice.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookRequest {

    private String isbn;

    private String title;

    private String genre;

    private String description;

    private String author;
}
