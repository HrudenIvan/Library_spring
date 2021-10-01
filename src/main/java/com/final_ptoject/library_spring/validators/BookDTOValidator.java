package com.final_ptoject.library_spring.validators;

import com.final_ptoject.library_spring.dto.BookDTO;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.time.LocalDate;

/**
 * Custom implementation of {@link Validator} interface to validate {@link BookDTO}
 */
@Service
public class BookDTOValidator implements Validator {

    /**
     * Method to check if given class supports by this validator
     * @param clazz class to be checked
     * @return true if supports, false - otherwise
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return BookDTO.class.isAssignableFrom(clazz);
    }

    /**
     * Validation method of {@link BookDTO} object.
     * @param target object to be validated
     * @param errors instance of {@link Errors}
     */
    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", "title.empty");
        BookDTO bookDTO = (BookDTO) target;
        if (bookDTO.getTitle().length() > 100) {
            errors.rejectValue("title", "title.too.long");
        }
        if (bookDTO.getReleaseDate() < 0) {
            errors.rejectValue("releaseDate", "releaseDate.negative");
        }
        if (bookDTO.getReleaseDate() > LocalDate.now().getYear()) {
            errors.rejectValue("releaseDate", "releaseDate.before");
        }
        if (bookDTO.getQuantity() <= 0) {
            errors.rejectValue("quantity", "quantity.zerOrLess");
        }
        if (bookDTO.getId() != 0) {
            int delta = bookDTO.getQuantity() - bookDTO.getQuantityOld();
            if (delta + bookDTO.getAvailable() < 0) {
                errors.rejectValue("quantity", "quantity.delta");
            }
        }
    }
}
