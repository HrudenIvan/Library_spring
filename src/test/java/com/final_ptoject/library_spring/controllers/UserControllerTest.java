package com.final_ptoject.library_spring.controllers;

import com.final_ptoject.library_spring.dto.BookOrderDTO;
import com.final_ptoject.library_spring.entities.*;
import com.final_ptoject.library_spring.services.BookOrderService;
import com.final_ptoject.library_spring.services.BookService;
import com.final_ptoject.library_spring.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static com.final_ptoject.library_spring.utils.Constants.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithUserDetails
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @MockBean
    private UserService userService;
    @MockBean
    private BookService bookService;
    @MockBean
    private BookOrderService bookOrderService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void userPageShouldReturnUserPage() throws Exception {
        User user = new User();
        List<BookOrderDTO> bookOrders = new ArrayList<>();
        when(userService.findUserById(anyLong())).thenReturn(user);
        when(bookOrderService.findUsersOngoingOrders(anyLong())).thenReturn(bookOrders);
        this.mockMvc.perform(get("/user"))
                .andExpect(status().isOk())
                .andExpect(view().name(USER_PAGE))
                .andExpect(model().attribute("bookOrders", bookOrders))
                .andExpect(model().attribute("userDTO", user));
    }

    @Test
    void bookOrderPageShouldReturnUserBookOrderPage() throws Exception {
        Book book = new Book();
        book.setAuthor(new Author());
        book.setPublisher(new Publisher());
        when(bookService.findBookById(anyLong())).thenReturn(book);
        this.mockMvc.perform(get("/user/books/order/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name(USER_BOOK_ORDER_PAGE))
                .andExpect(model().attributeExists("bookOrderDTO"));
    }

    @Test
    void saveBookOrderShouldRedirectToBooksPage() throws Exception {
        when(bookOrderService.saveBookOrder(any())).thenReturn(new BookOrderDTO());
        this.mockMvc.perform(post("/user/books/order"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(USER_BOOKS_REDIRECT));
    }

    @Test
    void availableBooksPageShouldReturnBooksPage() throws Exception {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("1");
        book.setReleaseDate(2021);
        book.setPublisher(new Publisher(1, "1"));
        book.setAuthor(new Author(1, "1", "1"));
        book.setQuantity(5);
        book.setAvailable(5);
        List<Book> bookList = new ArrayList<>();
        bookList.add(book);
        Page<Book> books = new PageImpl<>(bookList);
        when(bookService.findAllAvailableBooksPaginated(anyString(), anyString(), anyString(), any())).thenReturn(books);
        this.mockMvc.perform(get("/user/books"))
                .andExpect(status().isOk())
                .andExpect(view().name(USER_BOOKS_PAGE))
                .andExpect(model().attributeExists("bookPage", "pagingParam", "pageNumbers"));
    }
}