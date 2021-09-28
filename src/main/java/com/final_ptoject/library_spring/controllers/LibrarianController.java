package com.final_ptoject.library_spring.controllers;

import com.final_ptoject.library_spring.dto.BookOrderDTO;
import com.final_ptoject.library_spring.entities.AppUserDetails;
import com.final_ptoject.library_spring.entities.BookOrder;
import com.final_ptoject.library_spring.entities.User;
import com.final_ptoject.library_spring.services.BookOrderService;
import com.final_ptoject.library_spring.services.UserService;
import com.final_ptoject.library_spring.utils.DTOHelper;
import com.final_ptoject.library_spring.validators.BookOrderDTOValidator;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import static com.final_ptoject.library_spring.utils.Constants.*;
import static com.final_ptoject.library_spring.utils.Pagination.buildPageNumbers;

@NoArgsConstructor
@AllArgsConstructor(onConstructor_ = {@Autowired})
@Controller
@RequestMapping("/librarian")
public class LibrarianController {
    private static final Logger logger = LoggerFactory.getLogger(LibrarianController.class);
    UserService userService;
    BookOrderService bookOrderService;
    BookOrderDTOValidator bookOrderDTOValidator;

    @GetMapping
    public String librarianPage(Model model,
                                @RequestParam(required = false, defaultValue = "1") Integer page,
                                @RequestParam(required = false, defaultValue = "1") Integer pageUser,
                                @RequestParam(required = false, defaultValue = "5") Integer size) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String login = ((UserDetails) principal).getUsername();
        model.addAttribute("userDTO", userService.findUserByLogin(login));

        Page<BookOrder> currentPage = bookOrderService.findNewBookOrdersPageable(page - 1, size);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("pageNumbers", buildPageNumbers(currentPage.getTotalPages()));

        Page<User> usersCurrentPage = userService.findUsersWithOpenOrdersPageable(pageUser - 1, size);
        model.addAttribute("usersCurrentPage", usersCurrentPage);
        model.addAttribute("usersPageNumbers", buildPageNumbers(usersCurrentPage.getTotalPages()));

        return LIBRARIAN_PAGE;
    }

    @GetMapping("/orders/edit/{id}")
    public String editBookOrderPage(@PathVariable Long id, Model model) {
        BookOrderDTO bookOrderDTO = bookOrderService.findBookOrderById(id);
        setToNowIfOpenDateIsNull(bookOrderDTO);
        setReturnDateDependsOnOrderType(bookOrderDTO);
        model.addAttribute(bookOrderDTO);

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        long userId = ((AppUserDetails) principal).getId();
        String message = String.format("Librarian %s going to modify bookOrder id %s", userId, bookOrderDTO.getId());
        logger.info(message);

        return LIBRARIAN_ORDER_EDIT_PAGE;
    }

    private void setReturnDateDependsOnOrderType(BookOrderDTO bookOrderDTO) {
        if (bookOrderDTO.getCloseDate() == null) {
            if (bookOrderDTO.getOrderType().getId() == 1) {
                bookOrderDTO.setCloseDate(LocalDate.now());
            } else {
                bookOrderDTO.setCloseDate(LocalDate.now().plusDays(7));
            }
            bookOrderDTO.setOldCloseDate(bookOrderDTO.getCloseDate());
        }
    }

    private void setToNowIfOpenDateIsNull(BookOrderDTO bookOrderDTO) {
        if (bookOrderDTO.getOpenDate() == null) {
            bookOrderDTO.setOpenDate(LocalDate.now());
        }
    }

    @PostMapping("/orders/edit/{id}")
    public String updateBookOrder(@PathVariable Long id, @ModelAttribute BookOrderDTO bookOrderDTO, BindingResult errors, Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        long userId = ((AppUserDetails) principal).getId();

        bookOrderDTOValidator.validate(bookOrderDTO, errors);
        if (errors.hasErrors()) {
            model.addAttribute(bookOrderDTO);

            String message = String.format("Librarian %s failed to modify bookOrder id %s", userId, bookOrderDTO.getId());
            logger.info(message);

            return LIBRARIAN_ORDER_EDIT_PAGE;
        }

        bookOrderService.updateBookOrder(id, bookOrderDTO);

        String message = String.format("Librarian %s modified bookOrder id %s", userId, bookOrderDTO.getId());
        logger.info(message);

        return LIBRARIAN_PAGE_REDIRECT;
    }

    @GetMapping("/subscriptions/{id}")
    public String usersSubscription(@PathVariable Long id, Model model,
                                    @RequestParam(required = false, defaultValue = "1") Integer page,
                                    @RequestParam(required = false, defaultValue = "5") Integer size) {
        Page<BookOrder> noDTOPage = bookOrderService.findUserOpenOrdersPageable(id, page-1, size);
        Page<BookOrderDTO> currentPage = noDTOPage.map(DTOHelper::toDTO);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("pageNumbers", buildPageNumbers(currentPage.getTotalPages()));

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        long userId = ((AppUserDetails) principal).getId();
        String message = String.format("Librarian %s accessed subscription of user id %s", userId, id);
        logger.info(message);

        return LIBRARIAN_SUBSCRIPTION_PAGE;
    }
}
