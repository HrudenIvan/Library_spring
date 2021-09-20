package com.final_ptoject.library_spring.controllers;

import com.final_ptoject.library_spring.dto.UserDTO;
import com.final_ptoject.library_spring.services.UserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import static com.final_ptoject.library_spring.utils.Constants.*;
import static com.final_ptoject.library_spring.utils.DTOHelper.*;
import static com.final_ptoject.library_spring.utils.Validator.*;

@NoArgsConstructor
@AllArgsConstructor(onConstructor_ = {@Autowired})
@Controller
@RequestMapping("/administrator")
public class AdministratorController {
    private UserService userService;

    @GetMapping("/users")
    public String getAllUsers(Model model) {
        model.addAttribute("users", toDTO(userService.getAllUsers()));
        return ADMIN_ALL_USERS;
    }

    @GetMapping("/users/new")
    public String createUserForm(Model model) {
        model.addAttribute("user", new UserDTO());
        return ADMIN_CREATE_USER;
    }

    @PostMapping("/users")
    public String addNewUser(@ModelAttribute("user") UserDTO userDTO, Model model) {
        if (validateUser(userDTO, model)) {
            userService.saveUser(userDTO);
            return ADMIN_ALL_USERS_REDIRECT;
        } else {
            model.addAttribute("user", userDTO);
            return ADMIN_CREATE_USER;
        }

    }

    @GetMapping("/users/edit/{id}")
    public String editUserForm(@PathVariable long id, Model model) {
        model.addAttribute("user", toDTO(userService.findUserById(id)));
        return ADMIN_EDIT_USER;
    }

    @PostMapping("/users/{id}")
    public String editUser(@PathVariable long id, @ModelAttribute("user") UserDTO userDTO, Model model) {
        if (validateUser(userDTO, model)) {
            userService.updateUser(id, userDTO);
            return ADMIN_ALL_USERS_REDIRECT;
        } else {
            model.addAttribute("user", userDTO);
            return ADMIN_EDIT_USER;
        }

    }

    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable long id) {
        userService.deleteUser(id);
        return ADMIN_ALL_USERS_REDIRECT;
    }
}
