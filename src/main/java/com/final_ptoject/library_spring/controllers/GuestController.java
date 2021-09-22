package com.final_ptoject.library_spring.controllers;

import com.final_ptoject.library_spring.dto.UserDTO;
import com.final_ptoject.library_spring.entities.UserType;
import com.final_ptoject.library_spring.services.BookService;
import com.final_ptoject.library_spring.services.UserService;
import com.final_ptoject.library_spring.validators.UserDTOValidator;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import static com.final_ptoject.library_spring.utils.Constants.*;
import static com.final_ptoject.library_spring.utils.DTOHelper.bookListToDTO;

@NoArgsConstructor
@AllArgsConstructor(onConstructor_ = {@Autowired})
@Controller
public class GuestController {
    private BookService bookService;
    private UserService userService;
    private UserDTOValidator userDTOValidator;

    @GetMapping
    public String indexPage(Model model) {
        model.addAttribute("books", bookListToDTO(bookService.findAllAvailableBooks()));
        return INDEX_PAGE;
    }

    @GetMapping("/login")
    public String loginPage() {
        return LOGIN_PAGE;
    }

    @GetMapping("/registration")
    public String registrationPage(Model model) {
        model.addAttribute(new UserDTO());
        return REGISTRATION_PAGE;
    }

    @PostMapping("/registration")
    public String registerNewUser(@ModelAttribute UserDTO userDTO, BindingResult errors, Model model) {
        userDTOValidator.validate(userDTO, errors);
        if (userService.findUserByLogin(userDTO.getLogin()) != null) {
            model.addAttribute("userExist", true);
            model.addAttribute(userDTO);
            return REGISTRATION_PAGE;
        }
        if (errors.hasErrors()) {
            model.addAttribute(userDTO);
            return REGISTRATION_PAGE;
        }

        userDTO.setUserType(new UserType());
        userDTO.getUserType().setId(3);
        userDTO.setBlocked(false);
        userService.saveUser(userDTO);
        return "redirect: /login";
    }
}
