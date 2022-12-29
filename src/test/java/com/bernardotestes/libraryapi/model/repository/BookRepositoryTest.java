package com.bernardotestes.libraryapi.model.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import com.bernardotestes.libraryapi.model.entity.Book;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
public class BookRepositoryTest {
  
  @Autowired
  TestEntityManager entityManager;

  @Autowired
  BookRepository repository;

  @Test
  @DisplayName("Deve retornar verdadeiro quando existir um livro na base com o isbn informado.")
  public void returnTrueWhenIsbnExists() {
    
    String isbn = "123";
    Book book = createValidBook();
    entityManager.persist(book);

    boolean exists = repository.existsByIsbn(isbn);

    assertThat(exists).isTrue();
  }

  @Test
  @DisplayName("Deve retornar falso quando não existir um livro na base com o isbn informado.")
  public void returnFalseWhenIsbnExists() {
    
    String isbn = "123";

    boolean exists = repository.existsByIsbn(isbn);

    assertThat(exists).isFalse();
  }

  private Book createValidBook() {
      return Book.builder().isbn("123").author("Bernardo").title("Livro teste").build();
  }
}
