package com.final_ptoject.library_spring.services;

import com.final_ptoject.library_spring.dto.BookDTO;
import com.final_ptoject.library_spring.entities.Book;

import java.util.List;

public interface BookService {

    List<Book> getAllBooks();

    Book saveBook(BookDTO bookDTO);

    Book findBookById(Long id);

    Book updateBook(Long id, BookDTO bookDTO);

    void deleteBookById(Long id);
}
