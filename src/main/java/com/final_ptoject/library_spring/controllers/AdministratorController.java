package com.final_ptoject.library_spring.controllers;

import com.final_ptoject.library_spring.dto.AuthorDTO;
import com.final_ptoject.library_spring.dto.BookDTO;
import com.final_ptoject.library_spring.dto.PublisherDTO;
import com.final_ptoject.library_spring.dto.UserDTO;
import com.final_ptoject.library_spring.entities.*;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.final_ptoject.library_spring.utils.Constants.*;
import static com.final_ptoject.library_spring.utils.DTOHelper.*;
import static com.final_ptoject.library_spring.utils.Pagination.*;

@NoArgsConstructor
@AllArgsConstructor(onConstructor_ = {@Autowired})
@Controller
@RequestMapping("/administrator")
public class AdministratorController {
    private static final Logger logger = LoggerFactory.getLogger(AdministratorController.class);
    public static final String AUTHORS_ALIAS = "authors";
    public static final String PUBLISHERS_ALIAS = "publishers";
    public static final String CURRENT_PAGE = "currentPage";
    public static final String PAGE_NUMBERS = "pageNumbers";
    private UserService userService;
    private PublisherService publisherService;
    private AuthorService authorService;
    private BookService bookService;
    private AuthorDTOValidator authorDTOValidator;
    private UserDTOValidator userDTOValidator;
    private PublisherDTOValidator publisherDTOValidator;
    private BookDTOValidator bookDTOValidator;

    @GetMapping
    public String adminPage(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String login = ((UserDetails) principal).getUsername();
        model.addAttribute("userDTO", userService.findUserByLogin(login));
        return ADMIN_PAGE;
    }

    @PostMapping("/books/delete/{id}")
    public String deleteBookById(@PathVariable Long id) {
        bookService.deleteBookById(id);

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        long userId = ((AppUserDetails) principal).getId();
        String message = String.format("Administrator id %s deleted book id %s", userId, id);
        logger.info(message);

        return ADMIN_BOOKS_ALL_REDIRECT;
    }

    @PostMapping("/books/{id}")
    public String updateBook(@PathVariable Long id, @ModelAttribute BookDTO bookDTO, BindingResult errors, Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        long userId = ((AppUserDetails) principal).getId();

        bookDTOValidator.validate(bookDTO, errors);
        if (errors.hasErrors()) {
            model.addAttribute(bookDTO);
            model.addAttribute(AUTHORS_ALIAS, authorListToDTO(authorService.getAllAuthors()));
            model.addAttribute(PUBLISHERS_ALIAS, publisherListToDTO(publisherService.getAllPublishers()));

            String message = String.format("Administrator id %s failed to update book id %s", userId, id);
            logger.info(message);

            return ADMIN_BOOKS_EDIT_PAGE;
        }

        int delta = bookDTO.getQuantity() - bookDTO.getQuantityOld();
        bookDTO.setAvailable(bookDTO.getAvailable() + delta);
        bookService.updateBook(id, bookDTO);

        String message = String.format("Administrator id %s updated book id %s", userId, id);
        logger.info(message);

        return ADMIN_BOOKS_ALL_REDIRECT;
    }

    @PostMapping("/books")
    public String addNewBook(@ModelAttribute BookDTO bookDTO, BindingResult errors, Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        long userId = ((AppUserDetails) principal).getId();

        bookDTOValidator.validate(bookDTO, errors);
        if (errors.hasErrors()) {
            model.addAttribute(bookDTO);
            model.addAttribute(AUTHORS_ALIAS, authorListToDTO(authorService.getAllAuthors()));
            model.addAttribute(PUBLISHERS_ALIAS, publisherListToDTO(publisherService.getAllPublishers()));

            String message = String.format("Administrator id %s failed to add new book", userId);
            logger.info(message);

            return ADMIN_BOOKS_CREATE_PAGE;
        }

        bookDTO.setAvailable(bookDTO.getQuantity());
        Book book = bookService.saveBook(bookDTO);

        String message = String.format("Administrator id %s added new book id %s", userId, book.getId());
        logger.info(message);

        return ADMIN_BOOKS_ALL_REDIRECT;
    }

    @GetMapping("/books/edit/{id}")
    public String bookEditForm(@PathVariable Long id, Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        long userId = ((AppUserDetails) principal).getId();

        model.addAttribute(toDTO(bookService.findBookById(id)));
        model.addAttribute(AUTHORS_ALIAS, authorListToDTO(authorService.getAllAuthors()));
        model.addAttribute(PUBLISHERS_ALIAS, publisherListToDTO(publisherService.getAllPublishers()));

        String message = String.format("Administrator id %s going to edit book id %s", userId, id);
        logger.info(message);

        return ADMIN_BOOKS_EDIT_PAGE;
    }

    @GetMapping("/books/new")
    public String bookCreateForm(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        long userId = ((AppUserDetails) principal).getId();

        model.addAttribute(new BookDTO());
        model.addAttribute(AUTHORS_ALIAS, authorListToDTO(authorService.getAllAuthors()));
        model.addAttribute(PUBLISHERS_ALIAS, publisherListToDTO(publisherService.getAllPublishers()));

        String message = String.format("Administrator id %s going to add new book", userId);
        logger.info(message);

        return ADMIN_BOOKS_CREATE_PAGE;
    }

    @GetMapping("/books")
    public String allBooksPage(Model model, @RequestParam Optional<Integer> page, @RequestParam Optional<Integer> size,
                               @ModelAttribute Optional<BookPaginationParam> pagingParam) {
        BookPaginationParam bookPaginationParam = pagingParam.orElse(new BookPaginationParam());
        model.addAttribute("pagingParam", bookPaginationParam);
        int currentPage = page.orElse(1);
        int currentSize = size.orElse(5);
        Pageable pageable;
        if (bookPaginationParam.getSortOrder().equals("asc")) {
            pageable = PageRequest.of(currentPage - 1, currentSize, Sort.by(bookPaginationParam.getSortBy()));
        } else {
            pageable = PageRequest.of(currentPage - 1, currentSize, Sort.by(bookPaginationParam.getSortBy()).descending());
        }
        String title = "%" + bookPaginationParam.getTitle() + "%";
        String aLastName = "%" + bookPaginationParam.getAuthorLastName() + "%";
        String aFirstName = "%" + bookPaginationParam.getAuthorFirstName() + "%";
        Page<Book> bookPage = bookService.findAllBooksPaginated(title, aLastName, aFirstName, pageable);
        model.addAttribute("bookPage", bookPage);
        model.addAttribute(PAGE_NUMBERS, buildPageNumbers(bookPage.getTotalPages()));
        return ADMIN_BOOKS_ALL_PAGE;
    }

    @PostMapping("/authors/delete/{id}")
    public String deleteAuthor(@PathVariable Long id) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        long userId = ((AppUserDetails) principal).getId();

        authorService.deleteAuthorById(id);

        String message = String.format("Administrator id %s deleted author id %s", userId, id);
        logger.info(message);

        return ADMIN_AUTHORS_ALL_REDIRECT;
    }

    @PostMapping("/authors/{id}")
    public String updateAuthor(@PathVariable Long id, @ModelAttribute AuthorDTO authorDTO, BindingResult errors, Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        long userId = ((AppUserDetails) principal).getId();

        authorDTOValidator.validate(authorDTO, errors);
        if (errors.hasErrors()) {
            model.addAttribute(authorDTO);

            String message = String.format("Administrator id %s failed to update author id %s", userId, id);
            logger.info(message);

            return ADMIN_AUTHOR_EDIT_PAGE;
        }

        authorService.updateAuthor(id, authorDTO);

        String message = String.format("Administrator id %s updated author id %s", userId, id);
        logger.info(message);

        return ADMIN_AUTHORS_ALL_REDIRECT;
    }

    @PostMapping("/authors")
    public String addNewAuthor(@ModelAttribute AuthorDTO authorDTO, BindingResult errors, Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        long userId = ((AppUserDetails) principal).getId();

        authorDTOValidator.validate(authorDTO, errors);
        if (errors.hasErrors()) {
            model.addAttribute(authorDTO);

            String message = String.format("Administrator id %s failed to add new author", userId);
            logger.info(message);

            return ADMIN_AUTHOR_CREATE_PAGE;
        }

        Author author = authorService.saveAuthor(authorDTO);

        String message = String.format("Administrator id %s added new author id %s", userId, author.getId());
        logger.info(message);

        return ADMIN_AUTHORS_ALL_REDIRECT;
    }

    @GetMapping("/authors/edit/{id}")
    public String updateAuthorForm(@PathVariable Long id, Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        long userId = ((AppUserDetails) principal).getId();
        String message = String.format("Administrator id %s going to update author id %s", userId, id);
        logger.info(message);

        model.addAttribute(toDTO(authorService.findAuthorById(id)));

        return ADMIN_AUTHOR_EDIT_PAGE;
    }

    @GetMapping("/authors/new")
    public String createAuthorForm(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        long userId = ((AppUserDetails) principal).getId();
        String message = String.format("Administrator id %s going to add new author ", userId);
        logger.info(message);

        model.addAttribute(new AuthorDTO());

        return ADMIN_AUTHOR_CREATE_PAGE;
    }

    @GetMapping("/authors")
    public String getAllAuthors(Model model, @RequestParam(required = false, defaultValue = "1") Integer page,
                                @RequestParam(required = false, defaultValue = "5") Integer size) {
        Page<Author> currenPage = authorService.getAllAuthorsPageable(page - 1, size);
        model.addAttribute(CURRENT_PAGE, currenPage);
        model.addAttribute(PAGE_NUMBERS, buildPageNumbers(currenPage.getTotalPages()));
        return ADMIN_AUTHORS_ALL_PAGE;
    }

    @PostMapping("/publishers/delete/{id}")
    public String deletePublisher(@PathVariable Long id) {
        publisherService.deletePublisherById(id);

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        long userId = ((AppUserDetails) principal).getId();
        String message = String.format("Administrator id %s deleted publisher id %s", userId, id);
        logger.info(message);

        return ADMIN_PUBLISHERS_ALL_REDIRECT;
    }

    @PostMapping("/publishers/{id}")
    public String updatePublisher(@PathVariable Long id, @ModelAttribute PublisherDTO publisherDTO, BindingResult errors, Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        long userId = ((AppUserDetails) principal).getId();

        publisherDTOValidator.validate(publisherDTO, errors);
        if (errors.hasErrors()) {
            model.addAttribute(publisherDTO);

            String message = String.format("Administrator id %s failed to update publisher id %s", userId, id);
            logger.info(message);

            return ADMIN_PUBLISHER_EDIT_PAGE;
        }

        publisherService.updatePublisher(id, publisherDTO);

        String message = String.format("Administrator id %s updated publisher id %s", userId, id);
        logger.info(message);

        return ADMIN_PUBLISHERS_ALL_REDIRECT;
    }

    @GetMapping("/publishers/edit/{id}")
    public String editPublisherForm(@PathVariable Long id, Model model) {
        model.addAttribute(toDTO(publisherService.findPublisherById(id)));

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        long userId = ((AppUserDetails) principal).getId();
        String message = String.format("Administrator id %s going to update publisher id %s", userId, id);
        logger.info(message);

        return ADMIN_PUBLISHER_EDIT_PAGE;
    }

    @PostMapping("/publishers")
    public String addNewPublisher(@ModelAttribute PublisherDTO publisherDTO, BindingResult errors, Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        long userId = ((AppUserDetails) principal).getId();

        publisherDTOValidator.validate(publisherDTO, errors);
        if (errors.hasErrors()) {
            model.addAttribute(publisherDTO);

            String message = String.format("Administrator id %s failed to add new publisher", userId);
            logger.info(message);

            return ADMIN_PUBLISHER_CREATE_PAGE;
        }

        Publisher publisher = publisherService.savePublisher(publisherDTO);

        String message = String.format("Administrator id %s added new publisher id %s", userId, publisher.getId());
        logger.info(message);

        return ADMIN_PUBLISHERS_ALL_REDIRECT;
    }

    @GetMapping("/publishers/new")
    public String createPublisherForm(Model model) {
        model.addAttribute(new PublisherDTO());

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        long userId = ((AppUserDetails) principal).getId();
        String message = String.format("Administrator id %s going to add new publisher", userId);
        logger.info(message);

        return ADMIN_PUBLISHER_CREATE_PAGE;
    }

    @GetMapping("/publishers")
    public String getAllPublishers(Model model, @RequestParam(required = false, defaultValue = "1") Integer page,
                                   @RequestParam(required = false, defaultValue = "5") Integer size) {
        Page<Publisher> currentPage = publisherService.getAllPublishersPageable(page-1, size);
        model.addAttribute(CURRENT_PAGE, currentPage);
        model.addAttribute(PAGE_NUMBERS, buildPageNumbers(currentPage.getTotalPages()));
        return ADMIN_PUBLISHERS_ALL_PAGE;
    }

    @GetMapping("/users")
    public String getAllUsers(Model model, @RequestParam(required = false, defaultValue = "1") Integer page,
                              @RequestParam(required = false, defaultValue = "5") Integer size) {
        Page<User> currentPage = userService.getAllUsersPageable(page-1, size);
        model.addAttribute(CURRENT_PAGE, currentPage);
        model.addAttribute(PAGE_NUMBERS, buildPageNumbers(currentPage.getTotalPages()));
        return ADMIN_USERS_ALL_PAGE;
    }

    @GetMapping("/users/new")
    public String createUserForm(Model model) {
        model.addAttribute(new UserDTO());

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        long userId = ((AppUserDetails) principal).getId();
        String message = String.format("Administrator id %s going to add new user", userId);
        logger.info(message);

        return ADMIN_USER_CREATE_PAGE;
    }

    @PostMapping("/users")
    public String addNewUser(@ModelAttribute UserDTO userDTO, BindingResult errors, Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        long userId = ((AppUserDetails) principal).getId();

        userDTOValidator.validate(userDTO, errors);
        if (errors.hasErrors()) {
            model.addAttribute(userDTO);

            String message = String.format("Administrator id %s failed to add new user", userId);
            logger.info(message);

            return ADMIN_USER_CREATE_PAGE;
        }

        User user = userService.saveUser(userDTO);

        String message = String.format("Administrator id %s added new user id %s", userId, user.getId());
        logger.info(message);

        return ADMIN_USERS_ALL_REDIRECT;
    }

    @GetMapping("/users/edit/{id}")
    public String editUserForm(@PathVariable Long id, Model model) {
        model.addAttribute(toDTO(userService.findUserById(id)));

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        long userId = ((AppUserDetails) principal).getId();
        String message = String.format("Administrator id %s going to update user id %s", userId, id);
        logger.info(message);

        return ADMIN_USER_EDIT_PAGE;
    }

    @PostMapping("/users/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute UserDTO userDTO, BindingResult errors, Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        long userId = ((AppUserDetails) principal).getId();

        userDTOValidator.validate(userDTO, errors);
        if (errors.hasErrors()) {
            model.addAttribute(userDTO);

            String message = String.format("Administrator id %s failed to update user id %s", userId, id);
            logger.info(message);

            return ADMIN_USER_EDIT_PAGE;
        }

        userService.updateUser(id, userDTO);

        String message = String.format("Administrator id %s updated user id %s", userId, id);
        logger.info(message);

        return ADMIN_USERS_ALL_REDIRECT;
    }

    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        long userId = ((AppUserDetails) principal).getId();
        String message = String.format("Administrator id %s deleted user id %s", userId, id);
        logger.info(message);

        return ADMIN_USERS_ALL_REDIRECT;
    }
}
