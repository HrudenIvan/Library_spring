package com.final_ptoject.library_spring.controllers;

import com.final_ptoject.library_spring.dto.BookOrderDTO;
import com.final_ptoject.library_spring.services.BookOrderService;
import com.final_ptoject.library_spring.services.UserService;
import com.final_ptoject.library_spring.validators.BookOrderDTOValidator;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import static com.final_ptoject.library_spring.utils.Constants.*;

@NoArgsConstructor
@AllArgsConstructor(onConstructor_ = {@Autowired})
@Controller
@RequestMapping("/librarian")
public class LibrarianController {
    UserService userService;
    BookOrderService bookOrderService;
    BookOrderDTOValidator bookOrderDTOValidator;

    @GetMapping
    public String librarianPage(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String login = ((UserDetails) principal).getUsername();
        model.addAttribute("userDTO", userService.findUserByLogin(login));
        model.addAttribute("newBookOrders", bookOrderService.findNewBookOrders());
        model.addAttribute("users", userService.findUsersWithOpenOrders());
        return LIBRARIAN_PAGE;
    }

    @GetMapping("/orders/edit/{id}")
    public String editBookOrderPage(@PathVariable Long id, Model model) {
        BookOrderDTO bookOrderDTO = bookOrderService.findBookOrderById(id);
        setToNowIfOpenDateIsNull(bookOrderDTO);
        setReturnDateDependsOnOrderType(bookOrderDTO);
        model.addAttribute(bookOrderDTO);
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
    public String updateBookOrder(@PathVariable Long id, @ModelAttribute BookOrderDTO bookOrderDTO , BindingResult errors, Model model) {
        bookOrderDTOValidator.validate(bookOrderDTO, errors);
        if (errors.hasErrors()) {
            model.addAttribute(bookOrderDTO);
            return LIBRARIAN_ORDER_EDIT_PAGE;
        }

        bookOrderService.updateBookOrder(id, bookOrderDTO);
        return LIBRARIAN_PAGE_REDIRECT;
    }

    @GetMapping("/subscriptions/{id}")
    public String usersSubscription(@PathVariable Long id, Model model) {
        model.addAttribute("bookOrders", bookOrderService.findUserOpenOrders(id));
        return LIBRARIAN_SUBSCRIPTION_PAGE;
    }
}
