package com.final_ptoject.library_spring.repositories;

import com.final_ptoject.library_spring.entities.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository  extends JpaRepository<Book, Long> {

    @Query("SELECT b FROM Book b WHERE b.available > 0")
    List<Book> findAllAvailableBooks();

    @Query("SELECT b FROM Book b WHERE b.available > 0 AND b.title LIKE ?1 AND b.author.lastName LIKE ?2 AND b.author.firstName LIKE ?3")
    Page<Book> findAllAvailableBooksPaginated(String title, String aLastName, String aFirstName, Pageable pageable);

    @Query("SELECT b FROM Book b WHERE b.title LIKE ?1 AND b.author.lastName LIKE ?2 AND b.author.firstName LIKE ?3")
    Page<Book> findAllBooksPaginated(String title, String aLastName, String aFirstName, Pageable pageable);

    @Modifying
    @Query("UPDATE Book b SET b.available=b.available-1 WHERE b.id = ?1")
    void decrementBookAvailable(Long id);

    @Modifying
    @Query("UPDATE Book b SET b.available=b.available+1 WHERE b.id = ?1")
    void incrementBookAvailable(Long id);
}
