package com.final_ptoject.library_spring.validators;

import com.final_ptoject.library_spring.dto.UserDTO;
import com.final_ptoject.library_spring.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.*;

class UserDTOValidatorTest {
    private UserDTO userDTO;
    private final UserDTOValidator validator = new UserDTOValidator();
    private BindingResult errors;

    @BeforeEach
    void setUp() {
        userDTO = UserDTO
                .builder()
                .id(1L)
                .login("1")
                .firstName("1")
                .lastName("1")
                .penalty(0.0)
                .password("123123123123")
                .passwordConfirm("123123123123")
                .build();
        errors = new BeanPropertyBindingResult(userDTO,"userDTO");
    }

    @Test
    void supportsWhenValidatorSupportsClassThenReturnTrue() {
        assertTrue(validator.supports(UserDTO.class));
    }

    @Test
    void supportsWhenValidatorNotSupportsClassThenReturnFalse() {
        assertFalse(validator.supports(User.class));
    }

    @Test
    void validateWhenUserDTOIsValidThenErrorsIsEmpty() {
        validator.validate(userDTO, errors);
        assertFalse(errors.hasErrors());
    }

    @Test
    void validateWhenFirstNameNotValidThenErrorsHasFirstNameEmptyErrorCode() {
        userDTO.setFirstName(" ");
        validator.validate(userDTO, errors);
        assertTrue(errors.hasErrors());
        assertEquals("firstName.empty", errors.getAllErrors().get(0).getCode());
    }

    @Test
    void validateWhenLastNameNotValidThenErrorsHasLastNameEmptyErrorCode() {
        userDTO.setLastName(" ");
        validator.validate(userDTO, errors);
        assertTrue(errors.hasErrors());
        assertEquals("lastName.empty", errors.getAllErrors().get(0).getCode());
    }

    @Test
    void validateWhenLoginNotValidThenErrorsHasLoginRegexErrorCode() {
        userDTO.setLogin(" ");
        validator.validate(userDTO, errors);
        assertTrue(errors.hasErrors());
        assertEquals("login.regex", errors.getAllErrors().get(0).getCode());
    }

    @Test
    void validateWhenUserIsNewAndNotValidPasswordThenErrorsHasPasswordRegexErrorCode() {
        userDTO.setId(0);
        userDTO.setPassword(" ");
        userDTO.setPasswordConfirm(" ");
        validator.validate(userDTO, errors);
        assertTrue(errors.hasErrors());
        assertEquals("password.regex", errors.getAllErrors().get(0).getCode());
    }

    @Test
    void validateWhenUserIsNewAndPasswordsNotEqualsThenErrorsHasPasswordEqualsErrorCode() {
        userDTO.setId(0);
        userDTO.setPassword("321321321321");
        validator.validate(userDTO, errors);
        assertTrue(errors.hasErrors());
        assertEquals("password.equals", errors.getAllErrors().get(0).getCode());
    }

    @Test
    void validateWhenUserNotNewAndNotValidPasswordThenErrorsHasLoginRegexErrorCode() {
        userDTO.setPassword(" ");
        userDTO.setPasswordConfirm(" ");
        validator.validate(userDTO, errors);
        assertTrue(errors.hasErrors());
        assertEquals("password.regex", errors.getAllErrors().get(0).getCode());
    }

    @Test
    void validateWhenUserNotNewAndPasswordsNotEqualsThenErrorsHasPasswordEqualsErrorCode() {
        userDTO.setPassword("321321321321");
        validator.validate(userDTO, errors);
        assertTrue(errors.hasErrors());
        assertEquals("password.equals", errors.getAllErrors().get(0).getCode());
    }

    @Test
    void validateWhenUserNotNewAndPenaltyNegativeThenErrorsHasPenaltyNegativeErrorCode() {
        userDTO.setPenalty(-1.0);
        validator.validate(userDTO, errors);
        assertTrue(errors.hasErrors());
        assertEquals("penalty.negative", errors.getAllErrors().get(0).getCode());
    }
}