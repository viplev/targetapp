package dk.viplev.targetapp.port.inbound;

import dk.viplev.targetapp.adapter.inbound.rest.dto.BookDTO;
import dk.viplev.targetapp.adapter.inbound.rest.dto.BooksDTO;

public interface BookService {

    BooksDTO listBooks(int page, int size);

    BookDTO createBook(BookDTO bookDTO);

    BookDTO getBookById(Long id);

    BookDTO updateBook(Long id, BookDTO bookDTO);

    void deleteBook(Long id);

}
