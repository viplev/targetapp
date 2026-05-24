package dk.viplev.targetapp.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dk.viplev.targetapp.adapter.inbound.rest.dto.AuthorDTO;
import dk.viplev.targetapp.adapter.inbound.rest.dto.AuthorsDTO;
import dk.viplev.targetapp.adapter.inbound.rest.dto.BookDTO;
import dk.viplev.targetapp.adapter.inbound.rest.dto.BooksDTO;
import dk.viplev.targetapp.domain.exception.NotFoundException;
import dk.viplev.targetapp.domain.model.Author;
import dk.viplev.targetapp.domain.model.Book;
import dk.viplev.targetapp.port.inbound.AuthorService;
import dk.viplev.targetapp.port.outbound.AuthorRepository;
import dk.viplev.targetapp.port.outbound.BookRepository;

import java.net.URI;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    @Override
    @Transactional(readOnly = true)
    public AuthorsDTO listAuthors(int page, int size) {
        Page<Author> authorPage = authorRepository.findAll(PageRequest.of(page, size));
        List<AuthorDTO> items = authorPage.getContent().stream()
                .map(this::toDTO)
                .toList();

        String base = "/v1/authors";
        return buildAuthorsDTO(items, authorPage, base, page, size);
    }

    @Override
    @Transactional
    public AuthorDTO createAuthor(AuthorDTO authorDTO) {
        Author author = new Author();
        author.setName(authorDTO.getName());
        return toDTO(authorRepository.save(author));
    }

    @Override
    @Transactional(readOnly = true)
    public AuthorDTO getAuthorById(Long id) {
        return toDTO(findAuthor(id));
    }

    @Override
    @Transactional
    public AuthorDTO updateAuthor(Long id, AuthorDTO authorDTO) {
        Author author = findAuthor(id);
        if (authorDTO.getName() != null) {
            author.setName(authorDTO.getName());
        }
        return toDTO(authorRepository.save(author));
    }

    @Override
    @Transactional
    public void deleteAuthor(Long id) {
        Author author = findAuthor(id);
        authorRepository.delete(author);
    }

    @Override
    @Transactional(readOnly = true)
    public BooksDTO listBooksByAuthor(Long id, int page, int size) {
        Author author = findAuthor(id);
        Page<Book> bookPage = bookRepository.findByAuthor(author, PageRequest.of(page, size));
        List<BookDTO> items = bookPage.getContent().stream()
                .map(this::toBookDTO)
                .toList();

        String base = "/v1/authors/" + id + "/books";
        return buildBooksDTO(items, bookPage, base, page, size);
    }

    private Author findAuthor(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Author with id '" + id + "' not found"));
    }

    private AuthorDTO toDTO(Author author) {
        AuthorDTO dto = new AuthorDTO();
        dto.setId(author.getId());
        dto.setName(author.getName());
        return dto;
    }

    private BookDTO toBookDTO(Book book) {
        BookDTO dto = new BookDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setIsbn(book.getIsbn());
        dto.setDescription(book.getDescription());
        dto.setPublishedDate(book.getPublishedDate());
        dto.setPageCount(book.getPageCount());
        dto.setLanguage(book.getLanguage());
        dto.setGenre(book.getGenre());
        dto.setPrice(book.getPrice());
        dto.setAuthorId(book.getAuthor().getId());
        return dto;
    }

    private AuthorsDTO buildAuthorsDTO(List<AuthorDTO> items, Page<Author> authorPage,
                                       String base, int page, int size) {
        AuthorsDTO dto = new AuthorsDTO();
        dto.setAuthors(items);
        dto.setTotalElements(authorPage.getTotalElements());
        dto.setTotalPages(authorPage.getTotalPages());
        int lastPage = Math.max(0, authorPage.getTotalPages() - 1);
        dto.setSelf(URI.create(base + "?page=" + page + "&size=" + size));
        dto.setFirst(URI.create(base + "?page=0&size=" + size));
        dto.setLast(URI.create(base + "?page=" + lastPage + "&size=" + size));
        if (page > 0) {
            dto.setPrev(URI.create(base + "?page=" + (page - 1) + "&size=" + size));
        }
        if (page < lastPage) {
            dto.setNext(URI.create(base + "?page=" + (page + 1) + "&size=" + size));
        }
        return dto;
    }

    private BooksDTO buildBooksDTO(List<BookDTO> items, Page<Book> bookPage,
                                   String base, int page, int size) {
        BooksDTO dto = new BooksDTO();
        dto.setBooks(items);
        dto.setTotalElements(bookPage.getTotalElements());
        dto.setTotalPages(bookPage.getTotalPages());
        int lastPage = Math.max(0, bookPage.getTotalPages() - 1);
        dto.setSelf(URI.create(base + "?page=" + page + "&size=" + size));
        dto.setFirst(URI.create(base + "?page=0&size=" + size));
        dto.setLast(URI.create(base + "?page=" + lastPage + "&size=" + size));
        if (page > 0) {
            dto.setPrev(URI.create(base + "?page=" + (page - 1) + "&size=" + size));
        }
        if (page < lastPage) {
            dto.setNext(URI.create(base + "?page=" + (page + 1) + "&size=" + size));
        }
        return dto;
    }
}
