package com.bernardotestes.libraryapi.service.impl;

import com.bernardotestes.libraryapi.exception.BusinessException;
import com.bernardotestes.libraryapi.model.entity.Book;
import com.bernardotestes.libraryapi.model.repository.BookRepository;
import com.bernardotestes.libraryapi.service.BookService;

import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository repository;

    public BookServiceImpl(BookRepository repository) {
        this.repository = repository;
    }

    @Override
    public Book save(Book book) {
        if (repository.existsByIsbn(book.getIsbn())) 
            throw new BusinessException("Isbn já cadastrado.");
        return repository.save(book);
    }

    @Override
    public Optional<Book> getById(Long id) {
        return Optional.empty();
    }

    @Override
    public void delete(Book book) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Book update(Book book) {
        return book;
        // TODO Auto-generated method stub
        
    }
}
