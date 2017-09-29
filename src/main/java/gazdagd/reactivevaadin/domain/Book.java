/**
 * Copyright © 2017 Ericsson. A written permission from Ericsson is required to use this software.
 */
package gazdagd.reactivevaadin.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@ToString
@Document
public class Book
{
  @Id
  private String id;
  private String title;
  private String author;
  private Integer pages;
}
