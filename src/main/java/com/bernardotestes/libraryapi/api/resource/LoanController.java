package com.bernardotestes.libraryapi.api.resource;

import com.bernardotestes.libraryapi.api.dto.LoanDTO;
import com.bernardotestes.libraryapi.model.entity.Book;
import com.bernardotestes.libraryapi.model.entity.Loan;
import com.bernardotestes.libraryapi.service.BookService;
import com.bernardotestes.libraryapi.service.LoanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;
    private final BookService bookService;



    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long createLoan( @RequestBody @Valid LoanDTO dto){

        Book book = bookService.getBookByIsbn(dto.getIsbn()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,"Book not found."));

        Loan entity = Loan.builder().book(book).customer(dto.getCustomer()).loanDate(LocalDate.now()).build();

        entity = loanService.save(entity);

        return entity.getId();
    }
}
