package com.final_ptoject.library_spring.validators;

import com.final_ptoject.library_spring.dto.UserDTO;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Service
public class UserDTOValidator implements Validator {
    private static final String LOGIN_REGEX = "^[A-Za-z0-9]{1,20}$";
    private static final String PASSWORD_REGEX = "^[A-Za-z0-9]{10,}$";

    @Override
    public boolean supports(Class<?> clazz) {
        return UserDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "firstName.empty");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "lastName.empty");
        UserDTO userDTO = (UserDTO) target;
        if (!userDTO.getLogin().matches(LOGIN_REGEX)) {
            errors.rejectValue("login","login.regex");
        }
        if (userDTO.getId() == 0) {
            validatePasswords(errors, userDTO);
        } else {
            if (!userDTO.getPassword().isEmpty()) {
                validatePasswords(errors, userDTO);
            }
            if (userDTO.getPenalty() < 0) {
                errors.rejectValue("penalty", "penalty.negative");
            }
        }
    }

    private void validatePasswords(Errors errors, UserDTO userDTO) {
        if (!userDTO.getPassword().matches(PASSWORD_REGEX)) {
            errors.rejectValue("password", "password.regex");
        }
        if (!userDTO.getPassword().equals(userDTO.getPasswordConfirm())) {
            errors.rejectValue("passwordConfirm", "password.equals");
        }
    }
}
