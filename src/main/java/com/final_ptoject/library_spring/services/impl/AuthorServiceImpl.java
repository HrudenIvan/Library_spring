package com.final_ptoject.library_spring.services.impl;

import com.final_ptoject.library_spring.dto.AuthorDTO;
import com.final_ptoject.library_spring.entities.Author;
import com.final_ptoject.library_spring.repositories.AuthorRepository;
import com.final_ptoject.library_spring.services.AuthorService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of {@link AuthorService}
 */
@NoArgsConstructor
@AllArgsConstructor(onConstructor_ = {@Autowired})
@Service
public class AuthorServiceImpl implements AuthorService {
    private AuthorRepository authorRepository;

    @Override
    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    @Override
    public Author saveAuthor(AuthorDTO authorDTO) {
        Author author = new Author();
        author.setFirstName(authorDTO.getFirstName());
        author.setLastName(authorDTO.getLastName());
        return authorRepository.save(author);
    }

    @Override
    public Author findAuthorById(Long id) {
        return authorRepository.getById(id);
    }

    @Override
    public Author updateAuthor(Long id, AuthorDTO authorDTO) {
        Author author = authorRepository.getById(id);
        author.setFirstName(authorDTO.getFirstName());
        author.setLastName(authorDTO.getLastName());
        return authorRepository.save(author);
    }

    @Override
    public void deleteAuthorById(Long id) {
        authorRepository.deleteById(id);
    }

    @Override
    public Page<Author> getAllAuthorsPageable(Integer page, Integer size) {
        return authorRepository.findAll(PageRequest.of(page, size, Sort.by("lastName")));
    }
}
