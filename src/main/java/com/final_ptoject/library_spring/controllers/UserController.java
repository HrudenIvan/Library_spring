package com.final_ptoject.library_spring.controllers;

import com.final_ptoject.library_spring.dto.BookOrderDTO;
import com.final_ptoject.library_spring.entities.AppUserDetails;
import com.final_ptoject.library_spring.entities.OrderStatus;
import com.final_ptoject.library_spring.entities.User;
import com.final_ptoject.library_spring.services.BookOrderService;
import com.final_ptoject.library_spring.services.BookService;
import com.final_ptoject.library_spring.services.UserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

import static com.final_ptoject.library_spring.utils.Constants.*;
import static com.final_ptoject.library_spring.utils.DTOHelper.bookListToDTO;

@NoArgsConstructor
@AllArgsConstructor(onConstructor_ = {@Autowired})
@Controller
@RequestMapping("/user")
public class UserController {
    UserService userService;
    BookService bookService;
    BookOrderService bookOrderService;

    @GetMapping
    public String userPage(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        long userId = ((AppUserDetails) principal).getId();
        model.addAttribute("userDTO", userService.findUserById(userId));
        model.addAttribute("bookOrders", bookOrderService.findUsersOngoingOrders(userId));
        return USER_PAGE;
    }

    @GetMapping("/books")
    public String availableBooksPage(Model model) {
        model.addAttribute("books", bookListToDTO(bookService.findAllAvailableBooks()));
        return USER_BOOKS_PAGE;
    }

    @GetMapping("/books/order/{id}")
    public String bookOrderPage(@PathVariable Long id, Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        long userId = ((AppUserDetails) principal).getId();
        BookOrderDTO bookOrderDTO = new BookOrderDTO();
        bookOrderDTO.setBook(bookService.findBookById(id));
        bookOrderDTO.setUser(new User());
        bookOrderDTO.getUser().setId(userId);
        model.addAttribute(bookOrderDTO);
        return USER_BOOK_ORDER_PAGE;
    }

    @PostMapping("/books/order")
    public String saveBookOrder(@ModelAttribute BookOrderDTO bookOrderDTO) {
        bookOrderDTO.setOrderDate(LocalDateTime.now());
        bookOrderDTO.setOrderStatus(new OrderStatus());
        bookOrderDTO.getOrderStatus().setId(1);
        bookOrderService.saveBookOrder(bookOrderDTO);
        return USER_BOOKS_REDIRECT;
    }
}
