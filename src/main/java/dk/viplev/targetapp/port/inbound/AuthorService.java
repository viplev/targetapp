package dk.viplev.targetapp.port.inbound;

import dk.viplev.targetapp.adapter.inbound.rest.dto.AuthorDTO;
import dk.viplev.targetapp.adapter.inbound.rest.dto.AuthorsDTO;
import dk.viplev.targetapp.adapter.inbound.rest.dto.BooksDTO;

public interface AuthorService {

    AuthorsDTO listAuthors(int page, int size);

    AuthorDTO createAuthor(AuthorDTO authorDTO);

    AuthorDTO getAuthorById(Long id);

    AuthorDTO updateAuthor(Long id, AuthorDTO authorDTO);

    void deleteAuthor(Long id);

    BooksDTO listBooksByAuthor(Long id, int page, int size);

}
