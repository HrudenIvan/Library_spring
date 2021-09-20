package com.final_ptoject.library_spring.utils;

import com.final_ptoject.library_spring.dto.UserDTO;
import org.springframework.ui.Model;

public class Validator {
    private static final String LOGIN_REGEX = "^[A-Za-z0-9]{1,20}$";
    private static final String PASSWORD_REGEX = "^[A-Za-z0-9]{10,}$";

    private Validator() {
    }

    public static boolean validateUser(UserDTO userDTO, Model model) {
        boolean result;
        result = validateLogin(model, userDTO);
        result = validateFirstName(model, userDTO) && result;
        result = validateLastName(model, userDTO)  && result;
        if (userDTO.getId() == 0) {
            result = validatePassword(model, userDTO) && result;
        } else {
            if (!userDTO.getPassword().isEmpty()) {
                result = validatePassword(model, userDTO) && result;
            }
            result = validatePenalty(model, userDTO)  && result;
        }

        return result;
    }

    private static boolean validatePenalty(Model model, UserDTO userDTO) {
        if (userDTO.getPenalty() < 0) {
            model.addAttribute("penaltyErr",true);
            return false;
        }
        return true;
    }

    private static boolean validatePassword(Model model, UserDTO userDTO) {
        if (!userDTO.getPassword().matches(PASSWORD_REGEX)) {
            model.addAttribute("passwordRegexErr",true);
            return false;
        } else if (!userDTO.getPasswordConfirm().equals(userDTO.getPassword())) {
            model.addAttribute("passwordEqualsErr",true);
            return false;
        }
        return true;
    }

    private static boolean validateLastName(Model model, UserDTO userDTO) {
        if (userDTO.getLastName().trim().isEmpty()) {
            model.addAttribute("lastNameErr",true);
            return false;
        }
        return true;
    }

    private static boolean validateFirstName(Model model, UserDTO userDTO) {
        if (userDTO.getFirstName().trim().isEmpty()) {
            model.addAttribute("firstNameErr",true);
            return false;
        }
        return true;
    }

    private static boolean validateLogin(Model model, UserDTO userDTO) {
        if (!userDTO.getLogin().matches(LOGIN_REGEX)) {
            model.addAttribute("loginErr",true);
            return false;
        }
        return true;
    }

}
