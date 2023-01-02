package com.bernardotestes.libraryapi.service.impl;

import com.bernardotestes.libraryapi.exception.BusinessException;
import com.bernardotestes.libraryapi.model.entity.Loan;
import com.bernardotestes.libraryapi.model.repository.LoanRepository;
import com.bernardotestes.libraryapi.service.LoanService;
import org.springframework.stereotype.Service;

@Service
public class LoanServiceImpl implements LoanService {
    private final LoanRepository loanRepository;

    public LoanServiceImpl(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    @Override
    public Loan save(Loan loan) {
        if(loanRepository.existsByBookAndNotReturned(loan.getBook()))
            throw new BusinessException("Book already loaned.");
        return loanRepository.save(loan);
    }
}
