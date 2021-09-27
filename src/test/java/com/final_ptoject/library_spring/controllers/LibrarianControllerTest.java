package com.final_ptoject.library_spring.controllers;

import com.final_ptoject.library_spring.dto.BookOrderDTO;
import com.final_ptoject.library_spring.dto.UserDTO;
import com.final_ptoject.library_spring.entities.*;
import com.final_ptoject.library_spring.services.BookOrderService;
import com.final_ptoject.library_spring.services.UserService;
import com.final_ptoject.library_spring.validators.BookOrderDTOValidator;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.List;

import static com.final_ptoject.library_spring.utils.Constants.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithUserDetails
@SpringBootTest
@AutoConfigureMockMvc
class LibrarianControllerTest {
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @MockBean
    UserService userService;
    @MockBean
    BookOrderService bookOrderService;
    @MockBean
    BookOrderDTOValidator bookOrderDTOValidator;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void librarianPageShouldReturnLibrarianPage() throws Exception {
        User user = new User();
        List<User> userList = Collections.singletonList(user);
        Page<User> users = new PageImpl<>(userList);
        BookOrder bookOrder = new BookOrder();
        Book book = new Book();
        book.setPublisher(new Publisher());
        book.setAuthor(new Author());
        bookOrder.setBook(book);
        bookOrder.setOrderStatus(new OrderStatus());
        bookOrder.setOrderType(new OrderType());
        List<BookOrder> bookOrderList = Collections.singletonList(bookOrder);
        Page<BookOrder> bookOrders = new PageImpl<>(bookOrderList);
        when(userService.findUserByLogin(anyString())).thenReturn(new UserDTO());
        when(bookOrderService.findNewBookOrdersPageable(anyInt(), anyInt())).thenReturn(bookOrders);
        when(userService.findUsersWithOpenOrdersPageable(anyInt(), anyInt())).thenReturn(users);
        this.mockMvc.perform(get("/librarian"))
                .andExpect(status().isOk())
                .andExpect(view().name(LIBRARIAN_PAGE))
                .andExpect(model().attributeExists("userDTO", "currentPage", "pageNumbers", "usersCurrentPage", "usersPageNumbers"));
    }

    @Test
    void editBookOrderPageShouldReturnOrderEditPage() throws Exception {
        Book book = new Book();
        book.setPublisher(new Publisher());
        book.setAuthor(new Author());
        BookOrderDTO bookOrderDTO = BookOrderDTO
                .builder()
                .orderType(new OrderType())
                .orderStatus(new OrderStatus())
                .book(book)
                .build();
        when(bookOrderService.findBookOrderById(anyLong())).thenReturn(bookOrderDTO);
        this.mockMvc.perform(get("/librarian/orders/edit/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name(LIBRARIAN_ORDER_EDIT_PAGE))
                .andExpect(model().attributeExists("bookOrderDTO"));
    }

    @Test
    void updateBookOrderWhenOrderValidShouldRedirectToLibrarianPage() throws Exception {
        BookOrderDTO bookOrderDTO = new BookOrderDTO();
        BindingResult errors = mock(BindingResult.class);
        doNothing().when(bookOrderDTOValidator).validate(any(), any());
        when(bookOrderService.updateBookOrder(anyLong(),any())).thenReturn(bookOrderDTO);
        when(errors.hasErrors()).thenReturn(false);
        this.mockMvc.perform(post("/librarian/orders/edit/{id}", 1L, bookOrderDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(LIBRARIAN_PAGE_REDIRECT));
    }

    @Test
    void usersSubscriptionShouldReturnSubscriptionPage() throws Exception {
        BookOrder bookOrder = new BookOrder();
        Book book = new Book();
        book.setPublisher(new Publisher());
        book.setAuthor(new Author());
        bookOrder.setBook(book);
        bookOrder.setOrderStatus(new OrderStatus());
        bookOrder.setOrderType(new OrderType());
        List<BookOrder> bookOrderList = Collections.singletonList(bookOrder);
        Page<BookOrder> books = new PageImpl<>(bookOrderList);
        when(bookOrderService.findUserOpenOrdersPageable(anyLong(),anyInt(),anyInt())).thenReturn(books);
        this.mockMvc.perform(get("/librarian/subscriptions/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(view().name(LIBRARIAN_SUBSCRIPTION_PAGE))
                .andExpect(model().attributeExists("currentPage", "pageNumbers"));
    }

//    ????Another why?????
//    @Test
//    void updateBookOrderWhenOrderNotValidShouldOrderEditPage() throws Exception {
//        BookOrderDTO bookOrderDTO = new BookOrderDTO();
//        BindingResult errors = mock(BindingResult.class);
//        doNothing().when(bookOrderDTOValidator).validate(any(), any());
//        when(bookOrderService.updateBookOrder(anyLong(),any())).thenReturn(bookOrderDTO);
//        when(errors.hasErrors()).thenReturn(true);
//        this.mockMvc.perform(post("/librarian/orders/edit/{id}", 1L, bookOrderDTO))
//                .andExpect(status().isOk())
//                .andExpect(view().name(LIBRARIAN_ORDER_EDIT_PAGE))
//                .andExpect(model().attributeExists("bookOrderDTO"));
//    }
}