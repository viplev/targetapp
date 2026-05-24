package dk.viplev.targetapp.port.outbound;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dk.viplev.targetapp.domain.model.Author;
import dk.viplev.targetapp.domain.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Page<Book> findByAuthor(Author author, Pageable pageable);

}
