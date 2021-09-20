package com.final_ptoject.library_spring.controllers;

import com.final_ptoject.library_spring.dto.PublisherDTO;
import com.final_ptoject.library_spring.dto.UserDTO;
import com.final_ptoject.library_spring.entities.Publisher;
import com.final_ptoject.library_spring.services.PublisherService;
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
    private PublisherService publisherService;

    @PostMapping("/publishers/delete/{id}")
    public String deletePublisher(@PathVariable Long id) {
        publisherService.deletePublisherById(id);
        return ADMIN_ALL_PUBLISHERS_REDIRECT;
    }

    @PostMapping("/publishers/{id}")
    public String updatePublisher(@PathVariable Long id, @ModelAttribute("publisher") PublisherDTO publisherDTO) {
        publisherService.updatePublisher(id, publisherDTO);
        return ADMIN_ALL_PUBLISHERS_REDIRECT;
    }

    @GetMapping("/publishers/edit/{id}")
    public String editPublisherForm(@PathVariable Long id, Model model) {
        model.addAttribute("publisher", toDTO(publisherService.findPublisherById(id)));
        return ADMIN_EDIT_PUBLISHER_PAGE;
    }

    @PostMapping("/publishers")
    public String addNewPublisher(@ModelAttribute("publisher") PublisherDTO publisherDTO) {
        publisherService.savePublisher(publisherDTO);
        return ADMIN_ALL_PUBLISHERS_REDIRECT;
    }

    @GetMapping("/publishers/new")
    public String createPublisherForm(Model model) {
        model.addAttribute("publisher", toDTO(new Publisher()));
        return ADMIN_CREATE_PUBLISHER_PAGE;
    }

    @GetMapping("/publishers")
    public String getAllPublishers(Model model) {
        model.addAttribute("publishers", publisherListToDTO(publisherService.getAllPublishers()));
        return ADMIN_ALL_PUBLISHERS_PAGE;
    }

    @GetMapping("/users")
    public String getAllUsers(Model model) {
        model.addAttribute("users", userListToDTO(userService.getAllUsers()));
        return ADMIN_ALL_USERS_PAGE;
    }

    @GetMapping("/users/new")
    public String createUserForm(Model model) {
        model.addAttribute("user", new UserDTO());
        return ADMIN_CREATE_USER_PAGE;
    }

    @PostMapping("/users")
    public String addNewUser(@ModelAttribute("user") UserDTO userDTO, Model model) {
        if (validateUser(userDTO, model)) {
            userService.saveUser(userDTO);
            return ADMIN_ALL_USERS_REDIRECT;
        } else {
            model.addAttribute("user", userDTO);
            return ADMIN_CREATE_USER_PAGE;
        }

    }

    @GetMapping("/users/edit/{id}")
    public String editUserForm(@PathVariable long id, Model model) {
        model.addAttribute("user", toDTO(userService.findUserById(id)));
        return ADMIN_EDIT_USER_PAGE;
    }

    @PostMapping("/users/{id}")
    public String updateUser(@PathVariable long id, @ModelAttribute("user") UserDTO userDTO, Model model) {
        if (validateUser(userDTO, model)) {
            userService.updateUser(id, userDTO);
            return ADMIN_ALL_USERS_REDIRECT;
        } else {
            model.addAttribute("user", userDTO);
            return ADMIN_EDIT_USER_PAGE;
        }

    }

    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable long id) {
        userService.deleteUser(id);
        return ADMIN_ALL_USERS_REDIRECT;
    }
}
