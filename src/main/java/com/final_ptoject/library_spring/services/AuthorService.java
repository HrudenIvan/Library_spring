package com.final_ptoject.library_spring.services;

import com.final_ptoject.library_spring.dto.AuthorDTO;
import com.final_ptoject.library_spring.entities.Author;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AuthorService {

    List<Author> getAllAuthors();

    Author saveAuthor(AuthorDTO authorDTO);

    Author findAuthorById(Long id);

    Author updateAuthor(Long id, AuthorDTO authorDTO);

    void deleteAuthorById(Long id);

    Page<Author> getAllAuthorsPageable(Integer page, Integer size);
}
