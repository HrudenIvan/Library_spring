package com.final_ptoject.library_spring.controllers;

import com.final_ptoject.library_spring.dto.UserDTO;
import com.final_ptoject.library_spring.entities.Book;
import com.final_ptoject.library_spring.entities.User;
import com.final_ptoject.library_spring.entities.UserType;
import com.final_ptoject.library_spring.services.BookService;
import com.final_ptoject.library_spring.services.UserService;
import com.final_ptoject.library_spring.utils.Pagination.BookPaginationParam;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

import static com.final_ptoject.library_spring.utils.Constants.*;
import static com.final_ptoject.library_spring.utils.Pagination.buildPageNumbers;

/**
 * Controller for guest.
 */
@NoArgsConstructor
@AllArgsConstructor(onConstructor_ = {@Autowired})
@Controller
public class GuestController {
    private static final Logger logger = LoggerFactory.getLogger(GuestController.class);
    private BookService bookService;
    private UserService userService;
    private UserDTOValidator userDTOValidator;

    /**
     * Method to access index page. Handles GET request for URL "/"
     * @param model instance of {@link Model}
     * @param page number of requested page
     * @param size size of requested page
     * @param pagingParam instance of {@link BookPaginationParam}, which holds parameters for pagination
     * @return view "/index"
     */
    @GetMapping
    public String indexPage(Model model, @RequestParam Optional<Integer> page, @RequestParam Optional<Integer> size,
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
        Page<Book> bookPage = bookService.findAllAvailableBooksPaginated(title, aLastName, aFirstName, pageable);
        model.addAttribute("bookPage", bookPage);
        model.addAttribute("pageNumbers", buildPageNumbers(bookPage.getTotalPages()));
        return INDEX_PAGE;
    }

    /**
     * Method to access login page. Handles GET request for URL "/login"
     * @return view "/login"
     */
    @GetMapping("/login")
    public String loginPage() {
        return LOGIN_PAGE;
    }

    /**
     * Method to access registration page. Handles GET request for URL "/registration"
     * @param model instance of {@link Model}
     * @return view "/registration"
     */
    @GetMapping("/registration")
    public String registrationPage(Model model) {
        model.addAttribute(new UserDTO());
        return REGISTRATION_PAGE;
    }

    /**
     * Method for user registration. Handles POST request for URL "/registration".
     * If userDTO is valid, then redirects to "redirect:/login",
     * otherwise forwards to "/registration"
     * @param userDTO model attribute {@link UserDTO} for entity {@link User}
     * @param errors instance of {@link Model}
     * @param model instance of {@link BindingResult}
     * @return view
     */
    @PostMapping("/registration")
    public String registerNewUser(@ModelAttribute UserDTO userDTO, BindingResult errors, Model model) {
        userDTOValidator.validate(userDTO, errors);
        if (userService.findUserByLogin(userDTO.getLogin()) != null) {
            model.addAttribute("userExist", true);
            model.addAttribute(userDTO);
            String message = String.format("Registration failed for user with login \"%s\", this login already in use!", userDTO.getLogin());
            logger.info(message);
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
        String message = String.format("User with login %s successfully registered", userDTO.getLogin());
        logger.info(message);
        return LOGIN_PAGE_REDIRECT;
    }
}
