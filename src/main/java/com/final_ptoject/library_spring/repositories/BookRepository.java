package com.final_ptoject.library_spring.repositories;

import com.final_ptoject.library_spring.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository  extends JpaRepository<Book, Long> {

    @Query("SELECT b FROM Book b WHERE b.available > 0")
    List<Book> findAllAvailableBooks();

}
