package org.nikitactr.libraryservice.mapper;

import org.mapstruct.Mapper;
import org.nikitactr.libraryservice.model.Book;
import org.nikitactr.libraryservice.payload.reponse.BookResponse;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookMapper {

    List<BookResponse> booksToBookResponses(List<Book> books);
}