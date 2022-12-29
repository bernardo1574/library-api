package com.bernardotestes.libraryapi.service;


import com.bernardotestes.libraryapi.model.entity.Book;
import org.springframework.stereotype.Service;

@Service
public interface BookService {
    Book save(Book any);
}
