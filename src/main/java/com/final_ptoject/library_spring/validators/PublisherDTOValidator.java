package com.final_ptoject.library_spring.validators;

import com.final_ptoject.library_spring.dto.PublisherDTO;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Custom implementation of {@link Validator} interface to validate {@link PublisherDTO}
 */
@Service
public class PublisherDTOValidator implements Validator {

    /**
     * Method to check if given class supports by this validator
     * @param clazz class to be checked
     * @return true if supports, false - otherwise
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return PublisherDTO.class.isAssignableFrom(clazz);
    }

    /**
     * Validation method of {@link PublisherDTO} object
     * @param target object to be validated
     * @param errors instance of {@link Errors}
     */
    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "name.empty");
    }
}
