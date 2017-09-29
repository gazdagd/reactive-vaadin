package gazdagd.reactivevaadin.repositories;

import gazdagd.reactivevaadin.domain.Book;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface BookRepository extends ReactiveCrudRepository<Book, String>
{
}
