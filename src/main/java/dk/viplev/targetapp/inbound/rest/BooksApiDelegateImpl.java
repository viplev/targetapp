package dk.viplev.targetapp.inbound.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import dk.viplev.targetapp.adapter.inbound.rest.BooksApiDelegate;
import dk.viplev.targetapp.adapter.inbound.rest.dto.BookDTO;
import dk.viplev.targetapp.adapter.inbound.rest.dto.BooksDTO;
import dk.viplev.targetapp.port.inbound.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class BooksApiDelegateImpl implements BooksApiDelegate {

    private final BookService bookService;

    @Override
    public ResponseEntity<BooksDTO> listBooks(Integer page, Integer size) {
        return ResponseEntity.ok(bookService.listBooks(
                page != null ? page : 0,
                size != null ? size : 10));
    }

    @Override
    public ResponseEntity<BookDTO> createBook(BookDTO bookDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.createBook(bookDTO));
    }

    @Override
    public ResponseEntity<BookDTO> getBookById(Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @Override
    public ResponseEntity<BookDTO> updateBook(Long id, BookDTO bookDTO) {
        return ResponseEntity.ok(bookService.updateBook(id, bookDTO));
    }

    @Override
    public ResponseEntity<Void> deleteBook(Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}
