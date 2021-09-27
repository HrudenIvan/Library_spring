package com.final_ptoject.library_spring.validators;

import com.final_ptoject.library_spring.dto.AuthorDTO;
import com.final_ptoject.library_spring.entities.Author;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.*;

class AuthorDTOValidatorTest {
    private AuthorDTO authorDTO;
    private final AuthorDTOValidator validator = new AuthorDTOValidator();
    private BindingResult errors;

    @BeforeEach
    void setUp() {
        authorDTO = new AuthorDTO();
        errors = new BeanPropertyBindingResult(authorDTO,"authorDTO");
    }

    @Test
    void supportsWhenValidatorSupportsClassThenReturnTrue() {
        assertTrue(validator.supports(authorDTO.getClass()));
    }

    @Test
    void supportsWhenValidatorNotSupportsClassThenReturnFalse() {
        assertFalse(validator.supports(Author.class));
    }

    @Test
    void validateWhenFirstNameNotValidThenErrorsContainErrors() {
        authorDTO.setFirstName(" ");
        authorDTO.setLastName("1");
        validator.validate(authorDTO,errors);
        assertTrue(errors.hasErrors());
        assertEquals("firstName.empty", errors.getAllErrors().get(0).getCode());
    }

    @Test
    void validateWhenLastNameNotValidThenErrorsContainErrors() {
        authorDTO.setFirstName("1");
        authorDTO.setLastName(" ");
        validator.validate(authorDTO,errors);
        assertTrue(errors.hasErrors());
        assertEquals("lastName.empty", errors.getAllErrors().get(0).getCode());
    }

    @Test
    void validateWhenAllValidThenErrorsIsEmpty() {
        authorDTO.setFirstName("1");
        authorDTO.setLastName("1");
        validator.validate(authorDTO,errors);
        assertFalse(errors.hasErrors());
    }

}