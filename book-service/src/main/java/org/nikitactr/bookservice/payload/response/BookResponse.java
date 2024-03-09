package org.nikitactr.bookservice.payload.response;

import lombok.Data;

@Data
public class BookResponse {

    private String isbn;

    private String title;

    private String genre;

    private String description;

    private String author;
}
