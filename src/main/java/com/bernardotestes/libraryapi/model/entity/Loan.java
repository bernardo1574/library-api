package com.bernardotestes.libraryapi.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String customer;
    @JoinColumn
    @ManyToOne
    private Book book;
    @Column
    private LocalDate loanDate;
    @Column
    private Boolean returnedBook;
}
