package com.bernardotestes.libraryapi.model.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import com.bernardotestes.libraryapi.model.entity.Book;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

@ActiveProfiles("test")
@DataJpaTest
public class BookRepositoryTest {
  
  @Autowired
  TestEntityManager entityManager;

  @Autowired
  BookRepository repository;

  @Test
  @DisplayName("Deve retornar verdadeiro quando existir um livro na base com o isbn informado.")
  public void returnTrueWhenIsbnExistsTest() {
    
    String isbn = "123";
    Book book = createNewBook();
    entityManager.persist(book);

    boolean exists = repository.existsByIsbn(isbn);

    assertThat(exists).isTrue();
  }

  @Test
  @DisplayName("Deve retornar falso quando não existir um livro na base com o isbn informado.")
  public void returnFalseWhenIsbnExistsTest() {
    
    String isbn = "123";

    boolean exists = repository.existsByIsbn(isbn);

    assertThat(exists).isFalse();
  }

  @Test
  @DisplayName("Deve obeter um livro pelo id")
  public void findByIdBookTest() {
    
    Book book = createNewBook();
    entityManager.persist(book);

    Optional<Book> foundBook = repository.findById(book.getId());

    assertThat(foundBook.isPresent()).isTrue();

  }

  @Test
  @DisplayName("Deve salvar um livro")
  public void saveBookTest() {
    Book book = createNewBook();
    Book savedBook = repository.save(book);
    assertThat(savedBook.getId()).isNotNull();
  }
 
  @Test
  @DisplayName("Deve deletar um livro")
  public void deleteBookTest() {
    
    Book book = createNewBook();
    entityManager.persist(book);

    Book foundBook = entityManager.find(Book.class, book.getId());

    repository.delete(foundBook);

    Book deletedBook = entityManager.find(Book.class, book.getId());

    assertThat(deletedBook).isNull();

  }
  
  

  private Book createNewBook() {
      return Book.builder().isbn("123").author("Bernardo").title("Livro teste").build();
  }
}
