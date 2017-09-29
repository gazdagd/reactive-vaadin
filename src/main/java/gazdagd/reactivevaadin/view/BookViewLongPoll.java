/**
 * Copyright © 2017 Ericsson. A written permission from Ericsson is required to use this software.
 */
package gazdagd.reactivevaadin.view;

import com.vaadin.annotations.Push;;
import com.vaadin.server.ClientConnector;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.Registration;
import com.vaadin.shared.ui.ui.Transport;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import gazdagd.reactivevaadin.domain.Book;
import gazdagd.reactivevaadin.repositories.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.Disposable;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicLong;

@SpringUI(path = "/longpoll")
@Push(transport = Transport.LONG_POLLING)
@Slf4j
public class BookViewLongPoll extends UI
{

  @Autowired
  private BookRepository bookRepository;

  private HorizontalLayout horizontalLayout = new HorizontalLayout();
  private VerticalLayout verticalLayout = new VerticalLayout();
  private VerticalLayout content;

  private Label sumLabel = new Label();
  private AtomicLong sum = new AtomicLong();

  private Collection<Book> toRender = new LinkedList<>();

  private Object lock = new Object();

  private Window popup;

  private Disposable flux;

  @Override
  protected void init(VaadinRequest vaadinRequest)
  {
    setPollInterval(500);
    createPopup();
    content = new VerticalLayout();
    Button button = new Button("Fetch data");
    Button stop = new Button("Stop");
    button.addClickListener(clickEvent -> fetch());
    stop.addClickListener(clickEvent -> stop());
    horizontalLayout.addComponent(button);
    horizontalLayout.addComponent(stop);
    horizontalLayout.addComponent(new Label("Sum of pages: "));
    sumLabel.setValue(sum.toString());
    horizontalLayout.addComponent(sumLabel);
    content.addComponent(horizontalLayout);
    content.addComponent(verticalLayout);
    setContent(content);

    addPollListener(pollEvent -> {
      synchronized (lock){
        toRender.forEach((book) -> {
          verticalLayout.addComponent(new Label(book.getTitle()));
          sum.addAndGet(book.getPages());
        });
        sumLabel.setValue(sum.toString());
        toRender.clear();
      }
    });
  }

  private void fetch()
  {
    verticalLayout.removeAllComponents();
    sum.set(0);
    sumLabel.setValue(sum.toString());
    flux = bookRepository.findAll()
        .delayElements(Duration.ofMillis(5))
        .log()
        .doOnComplete(this::openPopup)
        .doOnCancel(() -> log.info("task cancelled"))
        .subscribe(this::addBook);
  }

  private void stop(){
    log.info("stopping...");
    if(flux != null){
      flux.dispose();
    }
  }

  private void addBook(Book book)
  {
    synchronized (lock){
      toRender.add(book);
    }
  }

  private void createPopup(){
    popup = new Window("Popup window");
    VerticalLayout subContent = new VerticalLayout();
    popup.setContent(subContent);
    subContent.addComponent(new Label("Fetch completed!"));
  }

  private void openPopup(){
    popup.center();
    addWindow(popup);
  }

  @Override
  public void close()
  {
    stop();
    super.close();
  }

  @Override
  public void detach()
  {
    stop();
    super.detach();
  }
}

