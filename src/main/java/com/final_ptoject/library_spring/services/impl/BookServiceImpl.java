package com.final_ptoject.library_spring.services.impl;

import com.final_ptoject.library_spring.dto.BookDTO;
import com.final_ptoject.library_spring.entities.Book;
import com.final_ptoject.library_spring.repositories.BookRepository;
import com.final_ptoject.library_spring.services.BookOrderService;
import com.final_ptoject.library_spring.services.BookService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of {@link BookService}
 */
@NoArgsConstructor
@AllArgsConstructor(onConstructor_ = {@Autowired})
@Service
public class BookServiceImpl implements BookService {
    private BookRepository bookRepository;

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Book saveBook(BookDTO bookDTO) {
        Book book = new Book();
        book.setTitle(bookDTO.getTitle());
        book.setQuantity(bookDTO.getQuantity());
        book.setAvailable(bookDTO.getAvailable());
        book.setReleaseDate(bookDTO.getReleaseDate());
        book.setAuthor(bookDTO.getAuthor());
        book.setPublisher(bookDTO.getPublisher());
        return bookRepository.save(book);
    }

    @Override
    public Book findBookById(Long id) {
        return bookRepository.getById(id);
    }

    @Override
    public Book updateBook(Long id, BookDTO bookDTO) {
        Book book = bookRepository.getById(id);
        book.setAuthor(bookDTO.getAuthor());
        book.setPublisher(bookDTO.getPublisher());
        book.setTitle(bookDTO.getTitle());
        book.setQuantity(bookDTO.getQuantity());
        book.setAvailable(bookDTO.getAvailable());
        book.setReleaseDate(bookDTO.getReleaseDate());
        return bookRepository.save(book);
    }

    @Override
    public void deleteBookById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public List<Book> findAllAvailableBooks() {
        return bookRepository.findAllAvailableBooks();
    }

    @Override
    public Page<Book> findAllAvailableBooksPaginated(String title, String aLastName, String aFirstName, Pageable pageable) {
        return bookRepository.findAllAvailableBooksPaginated(title, aLastName, aFirstName, pageable);
    }

    @Override
    public Page<Book> findAllBooksPaginated(String title, String aLastName, String aFirstName, Pageable pageable) {
        return bookRepository.findAllBooksPaginated(title, aLastName, aFirstName, pageable);
    }
}
