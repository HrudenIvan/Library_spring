package com.final_ptoject.library_spring.controllers;

import com.final_ptoject.library_spring.dto.UserDTO;
import com.final_ptoject.library_spring.entities.Book;
import com.final_ptoject.library_spring.entities.UserType;
import com.final_ptoject.library_spring.services.BookService;
import com.final_ptoject.library_spring.services.UserService;
import com.final_ptoject.library_spring.utils.Pagination.BookPaginationParam;
import com.final_ptoject.library_spring.validators.UserDTOValidator;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.final_ptoject.library_spring.utils.Constants.*;

@NoArgsConstructor
@AllArgsConstructor(onConstructor_ = {@Autowired})
@Controller
public class GuestController {
    private BookService bookService;
    private UserService userService;
    private UserDTOValidator userDTOValidator;

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
        if (bookPage.getTotalPages() > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, bookPage.getTotalPages())
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
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
