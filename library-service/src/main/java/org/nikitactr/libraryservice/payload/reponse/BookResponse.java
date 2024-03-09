package org.nikitactr.libraryservice.payload.reponse;

import lombok.Data;

@Data
public class BookResponse {

    private String isbn;

    private String title;

    private String genre;

    private String description;

    private String author;
}
