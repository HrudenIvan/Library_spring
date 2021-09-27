package com.final_ptoject.library_spring.validators;

import com.final_ptoject.library_spring.dto.BookDTO;
import com.final_ptoject.library_spring.entities.Author;
import com.final_ptoject.library_spring.entities.Book;
import com.final_ptoject.library_spring.entities.Publisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class BookDTOValidatorTest {
    private BookDTO bookDTO;
    private final BookDTOValidator validator = new BookDTOValidator();
    private BindingResult errors;

    @BeforeEach
    void setUp() {
        bookDTO = BookDTO
                .builder()
                .id(1L)
                .title("1")
                .quantity(5)
                .quantityOld(5)
                .available(5)
                .releaseDate(2000)
                .author(new Author(1L, "1", "1"))
                .publisher(new Publisher(1L, "1"))
                .build();
        errors = new BeanPropertyBindingResult(bookDTO,"bookDTO");
    }

    @Test
    void supportsWhenValidatorSupportsClassThenReturnTrue() {
        assertTrue(validator.supports(BookDTO.class));
    }

    @Test
    void supportsWhenValidatorNotSupportsClassThenReturnFalse() {
        assertFalse(validator.supports(Book.class));
    }

    @Test
    void validateWhenTitleValidThenErrorsIsEmpty() {
        validator.validate(bookDTO, errors);
        assertFalse(errors.hasErrors());
    }

    @Test
    void validateWhenTitleIsEmptyOrBlankThenErrorsHasTitleEmptyErrorCode() {
        bookDTO.setTitle(" ");
        validator.validate(bookDTO, errors);
        assertTrue(errors.hasErrors());
        assertEquals("title.empty", errors.getAllErrors().get(0).getCode());
    }

    @Test
    void validateWhenTitleIsTooLongThenErrorsHasTitleTooLongErrorCode() {
        bookDTO.setTitle("11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111");
        validator.validate(bookDTO, errors);
        assertTrue(errors.hasErrors());
        assertEquals("title.too.long", errors.getAllErrors().get(0).getCode());
    }

    @Test
    void validateWhenReleaseDateNegativeThenErrorsHasReleaseDateNegativeErrorCode() {
        bookDTO.setReleaseDate(-4);
        validator.validate(bookDTO, errors);
        assertTrue(errors.hasErrors());
        assertEquals("releaseDate.negative", errors.getAllErrors().get(0).getCode());
    }

    @Test
    void validateWhenReleaseDateAfterNowThenErrorsHasReleaseDateBeforeErrorCode() {
        bookDTO.setReleaseDate(LocalDate.now().getYear()+1);
        validator.validate(bookDTO, errors);
        assertTrue(errors.hasErrors());
        assertEquals("releaseDate.before", errors.getAllErrors().get(0).getCode());
    }

    @Test
    void validateWhenQuantityZeroOrNegativeThenErrorsHasQuantityZeroOrLessErrorCode() {
        bookDTO.setQuantity(-4);
        validator.validate(bookDTO, errors);
        assertTrue(errors.hasErrors());
        assertEquals("quantity.zerOrLess", errors.getAllErrors().get(0).getCode());
    }

    @Test
    void validateWhenQuantityDecrementsMoreThenAvailableAmountThenErrorsHasQuantityDeltaErrorCode() {
        bookDTO.setAvailable(1);
        bookDTO.setQuantity(1);
        validator.validate(bookDTO, errors);
        assertTrue(errors.hasErrors());
        assertEquals("quantity.delta", errors.getAllErrors().get(0).getCode());
    }
}