package org.project.manage.controller;

import java.util.List;

import org.project.manage.entities.Book;
import org.project.manage.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/book")
public class BookController {
	
    @Autowired
    private BookService bookService;
    
    @GetMapping
    public List<Book> findAll() {
        return bookService.findAll();
    }

}
