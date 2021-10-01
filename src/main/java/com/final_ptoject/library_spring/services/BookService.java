package com.final_ptoject.library_spring.services;

import com.final_ptoject.library_spring.dto.BookDTO;
import com.final_ptoject.library_spring.entities.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Interface for {@link Book} service
 */
public interface BookService {

    List<Book> getAllBooks();

    Book saveBook(BookDTO bookDTO);

    Book findBookById(Long id);

    Book updateBook(Long id, BookDTO bookDTO);

    void deleteBookById(Long id);

    List<Book> findAllAvailableBooks();

    Page<Book> findAllAvailableBooksPaginated(String title, String aLastName, String aFirstName, Pageable pageable);

    Page<Book> findAllBooksPaginated(String title, String aLastName, String aFirstName, Pageable pageable);
}
