package com.final_ptoject.library_spring.controllers;

import com.final_ptoject.library_spring.dto.BookOrderDTO;
import com.final_ptoject.library_spring.entities.AppUserDetails;
import com.final_ptoject.library_spring.entities.Book;
import com.final_ptoject.library_spring.entities.OrderStatus;
import com.final_ptoject.library_spring.entities.User;
import com.final_ptoject.library_spring.services.BookOrderService;
import com.final_ptoject.library_spring.services.BookService;
import com.final_ptoject.library_spring.services.UserService;
import com.final_ptoject.library_spring.utils.Pagination;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.final_ptoject.library_spring.utils.Constants.*;

@NoArgsConstructor
@AllArgsConstructor(onConstructor_ = {@Autowired})
@Controller
@RequestMapping("/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    UserService userService;
    BookService bookService;
    BookOrderService bookOrderService;

    @GetMapping
    public String userPage(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        long userId = ((AppUserDetails) principal).getId();
        model.addAttribute("userDTO", userService.findUserById(userId));
        model.addAttribute("bookOrders", bookOrderService.findUsersOngoingOrders(userId));

        String message = String.format("User id %s access private cabinet", userId);
        logger.info(message);

        return USER_PAGE;
    }

    @GetMapping("/books")
    public String availableBooksPage(Model model, @RequestParam Optional<Integer> page, @RequestParam Optional<Integer> size,
                                     @ModelAttribute Optional<Pagination.BookPaginationParam> pagingParam) {
        Pagination.BookPaginationParam bookPaginationParam = pagingParam.orElse(new Pagination.BookPaginationParam());
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
        String message = String.format("User id %s going to order book id %s", bookOrderDTO.getUser().getId(), bookOrderDTO.getBook().getId());
        logger.info(message);
        return USER_BOOK_ORDER_PAGE;
    }

    @PostMapping("/books/order")
    public String saveBookOrder(@ModelAttribute BookOrderDTO bookOrderDTO) {
        bookOrderDTO.setOrderDate(LocalDateTime.now());
        bookOrderDTO.setOrderStatus(new OrderStatus());
        bookOrderDTO.getOrderStatus().setId(1);
        bookOrderService.saveBookOrder(bookOrderDTO);
        String message = String.format("User id %s ordered book id %s", bookOrderDTO.getUser().getId(), bookOrderDTO.getBook().getId());
        logger.info(message);
        return USER_BOOKS_REDIRECT;
    }
}
