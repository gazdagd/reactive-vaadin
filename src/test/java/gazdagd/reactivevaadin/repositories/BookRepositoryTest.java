package gazdagd.reactivevaadin.repositories;

import gazdagd.reactivevaadin.domain.Book;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataMongoTest
public class BookRepositoryTest
{

  @Autowired
  BookRepository bookRepository;

  @Before
  public void setUp() throws Exception {
    bookRepository.deleteAll().block();
  }

  @Test
  public void testSave() throws Exception
  {
    Book book = new Book();
    book.setAuthor("david");
    book.setPages(40);
    book.setTitle("Reactive Vaadin");

    Book saved = bookRepository.save(book).block();

    assertNotNull(saved.getId());
  }
}