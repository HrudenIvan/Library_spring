package com.final_ptoject.library_spring.repositories;

import com.final_ptoject.library_spring.entities.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for {@link Author} entity
 */
@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
}
