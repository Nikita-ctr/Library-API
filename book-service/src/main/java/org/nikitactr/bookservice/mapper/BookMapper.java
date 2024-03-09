package org.nikitactr.bookservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.nikitactr.bookservice.model.Book;
import org.nikitactr.bookservice.payload.request.BookRequest;
import org.nikitactr.bookservice.payload.response.BookResponse;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookMapper {

    @Mapping(target = "id", ignore = true)
    Book bookRequestToBook(BookRequest bookRequest);

    BookResponse bookToBookResponse(Book book);

    List<BookResponse> booksToBookResponses(List<Book> books);
}