package com.final_ptoject.library_spring.controllers;

import com.final_ptoject.library_spring.dto.AuthorDTO;
import com.final_ptoject.library_spring.dto.BookDTO;
import com.final_ptoject.library_spring.dto.PublisherDTO;
import com.final_ptoject.library_spring.dto.UserDTO;
import com.final_ptoject.library_spring.services.AuthorService;
import com.final_ptoject.library_spring.services.BookService;
import com.final_ptoject.library_spring.services.PublisherService;
import com.final_ptoject.library_spring.services.UserService;
import com.final_ptoject.library_spring.validators.AuthorDTOValidator;
import com.final_ptoject.library_spring.validators.BookDTOValidator;
import com.final_ptoject.library_spring.validators.PublisherDTOValidator;
import com.final_ptoject.library_spring.validators.UserDTOValidator;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static com.final_ptoject.library_spring.utils.Constants.*;
import static com.final_ptoject.library_spring.utils.DTOHelper.*;

@NoArgsConstructor
@AllArgsConstructor(onConstructor_ = {@Autowired})
@Controller
@RequestMapping("/administrator")
public class AdministratorController {
    private UserService userService;
    private PublisherService publisherService;
    private AuthorService authorService;
    private BookService bookService;
    private AuthorDTOValidator authorDTOValidator;
    private UserDTOValidator userDTOValidator;
    private PublisherDTOValidator publisherDTOValidator;
    private BookDTOValidator bookDTOValidator;

    @PostMapping("/books/delete/{id}")
    public String deleteBookById(@PathVariable Long id) {
        bookService.deleteBookById(id);
        return ADMIN_BOOKS_ALL_REDIRECT;
    }

    @PostMapping("/books/{id}")
    public String updateBook(@PathVariable Long id, @ModelAttribute BookDTO bookDTO, BindingResult errors, Model model) {
        bookDTOValidator.validate(bookDTO, errors);
        if (errors.hasErrors()) {
            model.addAttribute(bookDTO);
            model.addAttribute("authors", authorListToDTO(authorService.getAllAuthors()));
            model.addAttribute("publishers", publisherListToDTO(publisherService.getAllPublishers()));
            return ADMIN_BOOKS_EDIT_PAGE;
        }

        int delta = bookDTO.getQuantity() - bookDTO.getQuantityOld();
        bookDTO.setAvailable(bookDTO.getAvailable() + delta);
        bookService.updateBook(id, bookDTO);
        return ADMIN_BOOKS_ALL_REDIRECT;
    }

    @PostMapping("/books")
    public String addNewBook(@ModelAttribute BookDTO bookDTO, BindingResult errors, Model model) {
        bookDTOValidator.validate(bookDTO, errors);
        if (errors.hasErrors()) {
            model.addAttribute(bookDTO);
            model.addAttribute("authors", authorListToDTO(authorService.getAllAuthors()));
            model.addAttribute("publishers", publisherListToDTO(publisherService.getAllPublishers()));
            return ADMIN_BOOKS_CREATE_PAGE;
        }

        bookDTO.setAvailable(bookDTO.getQuantity());
        bookService.saveBook(bookDTO);
        return ADMIN_BOOKS_ALL_REDIRECT;
    }

    @GetMapping("/books/edit/{id}")
    public String bookEditForm(@PathVariable Long id, Model model) {
        model.addAttribute(toDTO(bookService.findBookById(id)));
        model.addAttribute("authors", authorListToDTO(authorService.getAllAuthors()));
        model.addAttribute("publishers", publisherListToDTO(publisherService.getAllPublishers()));
        return ADMIN_BOOKS_EDIT_PAGE;
    }

    @GetMapping("/books/new")
    public String bookCreateForm(Model model) {
        model.addAttribute(new BookDTO());
        model.addAttribute("authors", authorListToDTO(authorService.getAllAuthors()));
        model.addAttribute("publishers", publisherListToDTO(publisherService.getAllPublishers()));
        return ADMIN_BOOKS_CREATE_PAGE;
    }

    @GetMapping("/books")
    public String allBooksPage(Model model) {
        model.addAttribute("books", bookListToDTO(bookService.getAllBooks()));
        return ADMIN_BOOKS_ALL_PAGE;
    }

    @PostMapping("/authors/delete/{id}")
    public String deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthorById(id);
        return ADMIN_AUTHORS_ALL_REDIRECT;
    }

    @PostMapping("/authors/{id}")
    public String updateAuthor(@PathVariable Long id, @ModelAttribute AuthorDTO authorDTO, BindingResult errors, Model model) {
        authorDTOValidator.validate(authorDTO, errors);
        if (errors.hasErrors()) {
            model.addAttribute(authorDTO);
            return ADMIN_AUTHOR_EDIT_PAGE;
        }

        authorService.updateAuthor(id, authorDTO);
        return ADMIN_AUTHORS_ALL_REDIRECT;
    }

    @PostMapping("/authors")
    public String addNewAuthor(@ModelAttribute AuthorDTO authorDTO, BindingResult errors, Model model) {
        authorDTOValidator.validate(authorDTO, errors);
        if (errors.hasErrors()) {
            model.addAttribute(authorDTO);
            return ADMIN_AUTHOR_CREATE_PAGE;
        }

        authorService.saveAuthor(authorDTO);
        return ADMIN_AUTHORS_ALL_REDIRECT;
    }

    @GetMapping("/authors/edit/{id}")
    public String updateAuthorForm(@PathVariable Long id, Model model) {
        model.addAttribute(toDTO(authorService.findAuthorById(id)));
        return ADMIN_AUTHOR_EDIT_PAGE;
    }

    @GetMapping("/authors/new")
    public String createAuthorForm(Model model) {
        model.addAttribute(new AuthorDTO());
        return ADMIN_AUTHOR_CREATE_PAGE;
    }

    @GetMapping("/authors")
    public String getAllAuthors(Model model) {
        model.addAttribute("authors", authorListToDTO(authorService.getAllAuthors()));
        return ADMIN_AUTHORS_ALL_PAGE;
    }

    @PostMapping("/publishers/delete/{id}")
    public String deletePublisher(@PathVariable Long id) {
        publisherService.deletePublisherById(id);
        return ADMIN_PUBLISHERS_ALL_REDIRECT;
    }

    @PostMapping("/publishers/{id}")
    public String updatePublisher(@PathVariable Long id, @ModelAttribute PublisherDTO publisherDTO, BindingResult errors, Model model) {
        publisherDTOValidator.validate(publisherDTO, errors);
        if (errors.hasErrors()) {
            model.addAttribute(publisherDTO);
            return ADMIN_PUBLISHER_EDIT_PAGE;
        }

        publisherService.updatePublisher(id, publisherDTO);
        return ADMIN_PUBLISHERS_ALL_REDIRECT;
    }

    @GetMapping("/publishers/edit/{id}")
    public String editPublisherForm(@PathVariable Long id, Model model) {
        model.addAttribute(toDTO(publisherService.findPublisherById(id)));
        return ADMIN_PUBLISHER_EDIT_PAGE;
    }

    @PostMapping("/publishers")
    public String addNewPublisher(@ModelAttribute PublisherDTO publisherDTO, BindingResult errors, Model model) {
        publisherDTOValidator.validate(publisherDTO, errors);
        if (errors.hasErrors()) {
            model.addAttribute(publisherDTO);
            return ADMIN_PUBLISHER_CREATE_PAGE;
        }

        publisherService.savePublisher(publisherDTO);
        return ADMIN_PUBLISHERS_ALL_REDIRECT;
    }

    @GetMapping("/publishers/new")
    public String createPublisherForm(Model model) {
        model.addAttribute(new PublisherDTO());
        return ADMIN_PUBLISHER_CREATE_PAGE;
    }

    @GetMapping("/publishers")
    public String getAllPublishers(Model model) {
        model.addAttribute("publishers", publisherListToDTO(publisherService.getAllPublishers()));
        return ADMIN_PUBLISHERS_ALL_PAGE;
    }

    @GetMapping("/users")
    public String getAllUsers(Model model) {
        model.addAttribute("users", userListToDTO(userService.getAllUsers()));
        return ADMIN_USERS_ALL_PAGE;
    }

    @GetMapping("/users/new")
    public String createUserForm(Model model) {
        model.addAttribute(new UserDTO());
        return ADMIN_USER_CREATE_PAGE;
    }

    @PostMapping("/users")
    public String addNewUser(@ModelAttribute UserDTO userDTO, BindingResult errors, Model model) {
        userDTOValidator.validate(userDTO, errors);
        if (errors.hasErrors()) {
            model.addAttribute(userDTO);
            return ADMIN_USER_CREATE_PAGE;
        }

        userService.saveUser(userDTO);
        return ADMIN_USERS_ALL_REDIRECT;
    }

    @GetMapping("/users/edit/{id}")
    public String editUserForm(@PathVariable Long id, Model model) {
        model.addAttribute(toDTO(userService.findUserById(id)));
        return ADMIN_USER_EDIT_PAGE;
    }

    @PostMapping("/users/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute UserDTO userDTO, BindingResult errors, Model model) {
        userDTOValidator.validate(userDTO, errors);
        if (errors.hasErrors()) {
            model.addAttribute(userDTO);
            return ADMIN_USER_EDIT_PAGE;
        }

        userService.updateUser(id, userDTO);
        return ADMIN_USERS_ALL_REDIRECT;
    }

    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ADMIN_USERS_ALL_REDIRECT;
    }
}
