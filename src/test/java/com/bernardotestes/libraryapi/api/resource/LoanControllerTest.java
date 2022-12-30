package com.bernardotestes.libraryapi.api.resource;

import java.time.LocalDate;
import java.util.Optional;

import com.bernardotestes.libraryapi.model.entity.Loan;
import com.bernardotestes.libraryapi.service.LoanService;
import org.antlr.v4.runtime.misc.LogManager;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.http.MediaType;

import com.bernardotestes.libraryapi.api.dto.LoanDTO;
import com.bernardotestes.libraryapi.model.entity.Book;
import com.bernardotestes.libraryapi.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(controllers = LoanController.class)
@AutoConfigureMockMvc
public class LoanControllerTest {

    static final String LOAN_API = "/api/loans";

    @Autowired
    MockMvc mvc;
    
    @MockBean
    private BookService bookService;
    @MockBean
    private LoanService loanService;;

    @Test
    @DisplayName("Deve realizar um empréstimo")
    public void createLoanTest() throws Exception {

      LoanDTO dto = LoanDTO.builder().isbn("123").customer("Bernardo").build();
      String json = new ObjectMapper().writeValueAsString(dto);

      Book book = Book.builder().id(1L).isbn("123").build();

      BDDMockito.given(bookService.getBookByIsbn("123"))
          .willReturn(Optional.of(book));

      Loan loan = Loan.builder().id(1L).customer("Bernardo").book(book).loanDate(LocalDate.now()).build();
      BDDMockito.given(loanService.save(Mockito.any(Loan.class))).willReturn(loan);
      
      MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(LOAN_API)
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(json);
      
      mvc.perform(request)
        .andExpect( status().isCreated() )
        .andExpect( content().string("1"));

    }

    @Test
    @DisplayName("Deve gerar bad request ao não existir o isbn.")
    public void notExistsIsbnTest() throws Exception{
        LoanDTO dto = LoanDTO.builder().isbn("123").customer("Bernardo").build();
        String json = new ObjectMapper().writeValueAsString(dto);

        BDDMockito.given(bookService.getBookByIsbn("123"))
                .willReturn(Optional.empty());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(LOAN_API)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect( status().isBadRequest() )
                .andExpect( jsonPath("errors", Matchers.hasSize(1)))
                .andExpect( jsonPath("errors[0]").value("Book not found."))
        ;
    }
}
