package com.final_ptoject.library_spring.validators;

import com.final_ptoject.library_spring.dto.PublisherDTO;
import com.final_ptoject.library_spring.entities.Publisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.*;

class PublisherDTOValidatorTest {
    private PublisherDTO publisherDTO;
    private final PublisherDTOValidator validator = new PublisherDTOValidator();
    private BindingResult errors;

    @BeforeEach
    void setUp() {
        publisherDTO = PublisherDTO
                .builder()
                .id(1)
                .name("1")
                .build();
        errors = new BeanPropertyBindingResult(publisherDTO, "publisherDTO");
    }

    @Test
    void supportsWhenValidatorSupportsClassThenReturnTrue() {
        assertTrue(validator.supports(PublisherDTO.class));
    }

    @Test
    void supportsWhenValidatorNotSupportsClassThenReturnFalse() {
        assertFalse(validator.supports(Publisher.class));
    }

    @Test
    void validateWhenNameIsValidThenErrorsIsEmpty() {
        validator.validate(publisherDTO, errors);
        assertFalse(errors.hasErrors());
    }

    @Test
    void validateWhenNameNotValidThenErrorsHasNameEmpty() {
        publisherDTO.setName(" ");
        validator.validate(publisherDTO, errors);
        assertTrue(errors.hasErrors());
        assertEquals("name.empty", errors.getAllErrors().get(0).getCode());
    }
}