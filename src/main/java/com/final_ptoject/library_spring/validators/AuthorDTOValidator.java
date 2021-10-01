package com.final_ptoject.library_spring.validators;

import com.final_ptoject.library_spring.dto.AuthorDTO;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Custom implementation of {@link Validator} interface to validate {@link AuthorDTO}
 */
@Service
public class AuthorDTOValidator implements Validator {

    /**
     * Method to check if given class supports by this validator
     * @param clazz class to be checked
     * @return true if supports, false - otherwise
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return AuthorDTO.class.isAssignableFrom(clazz);
    }

    /**
     * Validation method of {@link AuthorDTO} object
     * @param target object to be validated
     * @param errors instance of {@link Errors}
     */
    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "firstName.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "lastName.empty");
    }
}
