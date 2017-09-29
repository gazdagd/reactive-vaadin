package gazdagd.reactivevaadin;

import com.vaadin.spring.annotation.EnableVaadin;
import com.vaadin.spring.boot.annotation.EnableVaadinServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ReactiveVaadinApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReactiveVaadinApplication.class, args);
	}
}
