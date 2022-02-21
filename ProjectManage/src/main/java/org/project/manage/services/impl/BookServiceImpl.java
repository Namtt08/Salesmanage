package org.project.manage.services.impl;

import java.util.List;

import org.project.manage.entities.Book;
import org.project.manage.repository.BookRepository;
import org.project.manage.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService{
	
    @Autowired
    private BookRepository bookRepository;

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

}
