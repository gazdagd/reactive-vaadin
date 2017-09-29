/**
 * Copyright © 2017 Ericsson. A written permission from Ericsson is required to use this software.
 */
package gazdagd.reactivevaadin.bootstrap;

import gazdagd.reactivevaadin.domain.Book;
import gazdagd.reactivevaadin.repositories.BookRepository;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.stream.Stream;

@Component
public class InitData implements ApplicationListener<ContextRefreshedEvent>
{

  private final BookRepository bookRepository;

  public InitData(BookRepository bookRepository)
  {
    this.bookRepository = bookRepository;
  }

  @Override public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent)
  {
    initData();
  }

  private void initData(){
   ;
    bookRepository.saveAll(Flux.fromStream( Stream.iterate(1, i -> ++i)
        .limit(1000)
        .map(this::createBook))).blockLast();
  }

  private Book createBook(Integer i){
    Book book = new Book();
    book.setTitle("Title " + i);
    book.setPages(i);
    book.setAuthor("Author" + i);
    return book;
  }
}
