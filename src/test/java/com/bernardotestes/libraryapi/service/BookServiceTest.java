package com.bernardotestes.libraryapi.service;

import com.bernardotestes.libraryapi.exception.BusinessException;
import com.bernardotestes.libraryapi.model.entity.Book;
import com.bernardotestes.libraryapi.model.repository.BookRepository;
import com.bernardotestes.libraryapi.service.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest {
    BookService service;

    @MockBean
    BookRepository repository;

    @BeforeEach
    public void setUp(){
        this.service = new BookServiceImpl( repository );
    }

    @Test
    @DisplayName("Deve salvar um livro")
    public void saveBookTest(){
        Book book = createValidBook();
        Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(false); 
        Mockito.when( repository.save(book) ).thenReturn(Book.builder().id(1L).isbn("357").author("Bernardo").title("Livro teste").build());

        Book savedBook = service.save(book);

        assertThat(savedBook.getId()).isNotNull();
        assertThat(savedBook.getIsbn()).isEqualTo("357");
        assertThat(savedBook.getAuthor()).isEqualTo("Bernardo");
        assertThat(savedBook.getTitle()).isEqualTo("Livro teste");
    }

    @Test
    @DisplayName("Deve lançar erro de negocio ao tentar salvar um livro com isbn duplicado")
    public void shouldNotSaveABookIsbnTest() {
        Book book = createValidBook();
        Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(true);
        
        Throwable excpetion = Assertions.catchThrowable(() -> service.save(book));
        
        assertThat(excpetion)
            .isInstanceOf(BusinessException.class)
                .hasMessage("Isbn já cadastrado.");

        Mockito.verify(repository, Mockito.never()).save(book); // verificar se o metodo save esta sendo chamado.

    }

    @Test
    @DisplayName("Deve obter um livro por Id")
    public void getBookByIdTest() {
        Long id = 1L;
        Book book = createValidBook();
        book.setId(id);
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(book));

        Optional<Book> foundBook = service.getById(id);

        assertThat(foundBook.isPresent()).isTrue();
        assertThat(foundBook.get().getId()).isEqualTo(book.getId());
        assertThat(foundBook.get().getAuthor()).isEqualTo(book.getAuthor());
        assertThat(foundBook.get().getTitle()).isEqualTo(book.getTitle());
        assertThat(foundBook.get().getIsbn()).isEqualTo(book.getIsbn());
    }

    @Test
    @DisplayName("Deve retornar vazio quando o livro pesquisado pelo id não existir")
    public void bookNotFoundByIdTest() {
        Long id = 1L;
        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());

        Optional<Book> book = service.getById(id);

        assertThat(book.isPresent()).isFalse();
    }

    @Test
    @DisplayName("Deve deletar um livro pelo id")
    public void deleteBookByIdTest() {

        Book book = Book.builder().id(1L).build();

        org.junit.jupiter.api.Assertions.assertDoesNotThrow(() ->  service.delete(book));

        verify(repository, times(1)).delete(book);
    }
    
    @Test
    @DisplayName("Deve retornar erro ao tentar deletar um livro inexistente pelo id")
    public void notDeleteBookByIdTest() {
        
        Book book = new Book();
        
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> service.delete(book));
        
        verify(repository, Mockito.never()).delete(book);
    }

    @Test
    @DisplayName("Deve atualizar um livro")
    public void updateBookByIdTest() {
        Long id = 1L;
        Book updatingBook = Book.builder().id(id).build();

        Book updatedBook = createValidBook();
        updatedBook.setId(id);

        Mockito.when(repository.save(updatingBook)).thenReturn(updatedBook);
        
        Book book = service.update(updatingBook);

        assertThat(book.getId()).isEqualTo(updatedBook.getId());
        assertThat(book.getIsbn()).isEqualTo(updatedBook.getIsbn());
        assertThat(book.getAuthor()).isEqualTo(updatedBook.getAuthor());
        assertThat(book.getTitle()).isEqualTo(updatedBook.getTitle());
    }
    
    @Test
    @DisplayName("Deve retornar erro ao tentar atualizar um livro inexistente pelo id")
    public void notUpdateBookByIdTest() {
        
        Book book = new Book();
        
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> service.update(book));
        
        verify(repository, Mockito.never()).save(book);
    }

    @Test
    @DisplayName("Deve filtrar livros pelas propriedades.")
    public void findBookTest() {

        Book book = createValidBook();

        PageRequest pageRequest = PageRequest.of(0, 10);

        List<Book> asList = Collections.singletonList(book);
        Page<Book> page = new PageImpl<>(asList, pageRequest, 1);
        when(
            repository.findAll(Mockito.any(), Mockito.<PageRequest>any()))
        .thenReturn(page);

        Page<Book> result = service.find(book, pageRequest);

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).isEqualTo(asList);
        assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(result.getPageable().getPageSize()).isEqualTo(10);
    }

    @Test
    @DisplayName("Deve obter um livro pelo Isbn")
    public void getBookByIsbnTest(){
        String isbn = "132";

        when(repository.findByIsbn(isbn)).thenReturn(Optional.of(Book.builder().id(1L).isbn(isbn).build()));

        Optional<Book> book  =service.getBookByIsbn(isbn);

        assertThat(book.isPresent()).isTrue();
        assertThat(book.get().getId()).isEqualTo(1L);
        assertThat(book.get().getIsbn()).isEqualTo(isbn);

        verify(repository, times(1)).findByIsbn(isbn);

    }

    private Book createValidBook() {
        return Book.builder().isbn("357").author("Bernardo").title("Livro teste").build();
    }
}
