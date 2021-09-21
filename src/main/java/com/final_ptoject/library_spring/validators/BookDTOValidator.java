package com.final_ptoject.library_spring.validators;

import com.final_ptoject.library_spring.dto.BookDTO;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.time.LocalDate;

@Service
public class BookDTOValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return BookDTO.class.isAssignableFrom(clazz);
    }

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
