package com.bernardotestes.libraryapi.model.repository;

import com.bernardotestes.libraryapi.model.entity.Book;
import com.bernardotestes.libraryapi.model.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    @Query(value = " select case when ( count(l.id) > 0 ) then true else false end " +
            " from Loan l where l.book = :book and ( l.returnedBook is null or l.returnedBook is false ) ")
    boolean existsByBookAndNotReturned( @Param("book") Book book );
}
