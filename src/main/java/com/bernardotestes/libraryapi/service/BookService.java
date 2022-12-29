package com.bernardotestes.libraryapi.service;


import com.bernardotestes.libraryapi.model.entity.Book;

import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public interface BookService {
    
    Book save(Book any);

    Optional<Book> getById(Long id);

    void delete(Book book);

    Book update(Book book);
}
