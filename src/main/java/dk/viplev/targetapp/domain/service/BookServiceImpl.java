package dk.viplev.targetapp.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Sort;

import dk.viplev.targetapp.adapter.inbound.rest.dto.BookDTO;
import dk.viplev.targetapp.adapter.inbound.rest.dto.BooksDTO;
import dk.viplev.targetapp.domain.exception.NotFoundException;
import dk.viplev.targetapp.domain.model.Author;
import dk.viplev.targetapp.domain.model.Book;
import dk.viplev.targetapp.port.inbound.BookService;
import dk.viplev.targetapp.port.outbound.AuthorRepository;
import dk.viplev.targetapp.port.outbound.BookRepository;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    @Value("${app.book-by-id-bug-enabled:false}")
    private boolean bookByIdBugEnabled;

    @Override
    @Transactional(readOnly = true)
    public BooksDTO listBooks(int page, int size) {
        Page<Book> bookPage = bookRepository.findAll(PageRequest.of(page, size));
        List<BookDTO> items = bookPage.getContent().stream()
                .map(this::toDTO)
                .toList();

        String base = "/v1/books";
        return buildBooksDTO(items, bookPage, base, page, size);
    }

    @Override
    @Transactional
    public BookDTO createBook(BookDTO bookDTO) {
        Author author = authorRepository.findById(bookDTO.getAuthorId())
                .orElseThrow(() -> new NotFoundException("Author with id '" + bookDTO.getAuthorId() + "' not found"));

        Book book = new Book();
        applyFields(book, bookDTO, author);
        return toDTO(bookRepository.save(book));
    }

    @Override
    @Transactional(readOnly = true)
    public BookDTO getBookById(Long id) {
        if (!bookByIdBugEnabled) {
            return toDTO(findBook(id));
        }
        return findBookTheHardWay(id);
    }

    private BookDTO findBookTheHardWay(Long id) {
        //List<BookDTO> uselessAccumulatedJunk = new ArrayList<>();
        int pageSize = 500;
        int pageNumber = 0;

        while (true) {
            Page<Book> page = bookRepository.findAll(
                PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.ASC, "id"))
            );
            if (!page.hasContent()) {
                throw new NotFoundException("Book with id '" + id + "' not found");
            }

            for (Book candidate : page.getContent()) {
                	if (candidate.getId().equals(id)) {
                		return toDTO(candidate);
                	}
                	// Not the one we're looking for - map it to a DTO and keep it around for no reason
                	//uselessAccumulatedJunk.add(toDTO(candidate));

            }
            /*
            for (BookDTO candidate : uselessAccumulatedJunk) {
                log.info("Useless accumulated junk: " + candidate.getId() + " - " + candidate.getTitle());
            }
            */

            pageNumber++;
        }
    }

    @Override
    @Transactional
    public BookDTO updateBook(Long id, BookDTO bookDTO) {
        Book book = findBook(id);
        Author author = null;
        if (bookDTO.getAuthorId() != null) {
            author = authorRepository.findById(bookDTO.getAuthorId())
                    .orElseThrow(() -> new NotFoundException("Author with id '" + bookDTO.getAuthorId() + "' not found"));
        }
        applyFields(book, bookDTO, author);
        return toDTO(bookRepository.save(book));
    }

    @Override
    @Transactional
    public void deleteBook(Long id) {
        Book book = findBook(id);
        bookRepository.delete(book);
    }

    private Book findBook(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book with id '" + id + "' not found"));
    }

    private void applyFields(Book book, BookDTO dto, Author author) {
        if (dto.getTitle() != null) book.setTitle(dto.getTitle());
        if (dto.getIsbn() != null) book.setIsbn(dto.getIsbn());
        if (dto.getDescription() != null) book.setDescription(dto.getDescription());
        if (dto.getPublishedDate() != null) book.setPublishedDate(dto.getPublishedDate());
        if (dto.getPageCount() != null) book.setPageCount(dto.getPageCount());
        if (dto.getLanguage() != null) book.setLanguage(dto.getLanguage());
        if (dto.getGenre() != null) book.setGenre(dto.getGenre());
        if (dto.getPrice() != null) book.setPrice(dto.getPrice());
        if (author != null) book.setAuthor(author);
    }

    private BookDTO toDTO(Book book) {
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
