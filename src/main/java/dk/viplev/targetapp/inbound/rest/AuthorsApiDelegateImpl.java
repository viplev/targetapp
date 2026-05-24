package dk.viplev.targetapp.inbound.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import dk.viplev.targetapp.adapter.inbound.rest.AuthorsApiDelegate;
import dk.viplev.targetapp.adapter.inbound.rest.dto.AuthorDTO;
import dk.viplev.targetapp.adapter.inbound.rest.dto.AuthorsDTO;
import dk.viplev.targetapp.adapter.inbound.rest.dto.BooksDTO;
import dk.viplev.targetapp.port.inbound.AuthorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthorsApiDelegateImpl implements AuthorsApiDelegate {

    private final AuthorService authorService;

    @Override
    public ResponseEntity<AuthorsDTO> listAuthors(Integer page, Integer size) {
        return ResponseEntity.ok(authorService.listAuthors(
                page != null ? page : 0,
                size != null ? size : 10));
    }

    @Override
    public ResponseEntity<AuthorDTO> createAuthor(AuthorDTO authorDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authorService.createAuthor(authorDTO));
    }

    @Override
    public ResponseEntity<AuthorDTO> getAuthorById(Long id) {
        return ResponseEntity.ok(authorService.getAuthorById(id));
    }

    @Override
    public ResponseEntity<AuthorDTO> updateAuthor(Long id, AuthorDTO authorDTO) {
        return ResponseEntity.ok(authorService.updateAuthor(id, authorDTO));
    }

    @Override
    public ResponseEntity<Void> deleteAuthor(Long id) {
        authorService.deleteAuthor(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<BooksDTO> listBooksByAuthor(Long id, Integer page, Integer size) {
        return ResponseEntity.ok(authorService.listBooksByAuthor(id,
                page != null ? page : 0,
                size != null ? size : 10));
    }
}
