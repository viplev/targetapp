package dk.viplev.targetapp.port.outbound;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dk.viplev.targetapp.domain.model.Author;

@Repository
public interface AuthorRepository extends JpaRepository<Author, UUID> {

}
