package com.bernardotestes.libraryapi.service;

import com.bernardotestes.libraryapi.exception.BusinessException;
import com.bernardotestes.libraryapi.model.entity.Book;
import com.bernardotestes.libraryapi.model.entity.Loan;
import com.bernardotestes.libraryapi.model.repository.LoanRepository;
import com.bernardotestes.libraryapi.service.impl.BookServiceImpl;
import com.bernardotestes.libraryapi.service.impl.LoanServiceImpl;
import org.antlr.v4.runtime.misc.LogManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class LoanServiceTest {

    private LoanService service;
    @MockBean
    private LoanRepository loanRepository;

    @BeforeEach
    public void setUp(){
        this.service = new LoanServiceImpl( loanRepository );
    }

    @Test
    @DisplayName("Deve salvar um empréstimo")
    public void saveLoanTest(){
        Book book = Book.builder().id(1L).build();
        String customer = "Bernardo";

        Loan savingLoan = Loan.builder()
                .book(book)
                .customer(customer)
                .loanDate(LocalDate.now())
                .build();

        Loan savedLoan = Loan.builder()
                .id(1L)
                .book(book)
                .customer(customer)
                .loanDate(LocalDate.now())
                .build();

        when(loanRepository.existsByBookAndNotReturned(book)).thenReturn(false);
        when(loanRepository.save(savingLoan)).thenReturn(savedLoan);

        Loan loan = service.save(savingLoan);

        assertThat(loan.getId()).isEqualTo(savedLoan.getId());
        assertThat(loan.getBook().getId()).isEqualTo(savedLoan.getBook().getId());
        assertThat(loan.getLoanDate()).isEqualTo(savedLoan.getLoanDate());
        assertThat(loan.getCustomer()).isEqualTo(savedLoan.getCustomer());

    }

    @Test
    @DisplayName("Deve gerar error de negócio ao tentar salvar um empréstimo com livro emprestado")
    public void LoanedBooksaveTest(){
        Book book = Book.builder().id(1L).build();
        String customer = "Bernardo";

        Loan loanedBook = Loan.builder()
                .book(book)
                .customer(customer)
                .loanDate(LocalDate.now())
                .build();
        when(loanRepository.existsByBookAndNotReturned(book)).thenReturn(true);

        Throwable exception = catchThrowable(() -> service.save(loanedBook));

        assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage("Book already loaned.");
        verify(loanRepository, never()).save(loanedBook);
    }
}
