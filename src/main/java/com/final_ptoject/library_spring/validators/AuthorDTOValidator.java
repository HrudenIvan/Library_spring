package com.final_ptoject.library_spring.validators;

import com.final_ptoject.library_spring.dto.AuthorDTO;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Service
public class AuthorDTOValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return AuthorDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "firstName.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "lastName.empty");
    }
}
