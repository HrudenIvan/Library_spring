package com.final_ptoject.library_spring.controllers;

import com.final_ptoject.library_spring.dto.UserDTO;
import com.final_ptoject.library_spring.entities.Author;
import com.final_ptoject.library_spring.entities.Book;
import com.final_ptoject.library_spring.entities.Publisher;
import com.final_ptoject.library_spring.services.BookService;
import com.final_ptoject.library_spring.services.UserService;
import com.final_ptoject.library_spring.validators.UserDTOValidator;
import org.junit.jupiter.api.BeforeAll;
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

import java.util.ArrayList;
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
class GuestControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @MockBean
    private UserService userService;
    @MockBean
    private BookService bookService;
    @MockBean
    private UserDTOValidator userDTOValidator;
    private static Page<Book> books;
    private static UserDTO userDTO1;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
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
        book = new Book();
        book.setId(2L);
        book.setTitle("2");
        book.setReleaseDate(2020);
        book.setPublisher(new Publisher(2L, "2"));
        book.setAuthor(new Author(2L, "2", "2"));
        book.setQuantity(5);
        book.setAvailable(5);
        bookList.add(book);
        books = new PageImpl<>(bookList);
        userDTO1 = UserDTO.builder().build();
    }

    @Test
    void indexPageShouldReturnIndexPage() throws Exception {
        when(bookService.findAllAvailableBooksPaginated(anyString(), anyString(), anyString(), any())).thenReturn(books);
        this.mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name(INDEX_PAGE))
                .andExpect(model().attribute("bookPage", books));
    }

    @Test
    void loginPageShouldReturnLoginPage () throws Exception {
        this.mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name(LOGIN_PAGE));
    }

    @Test
    void registrationPageShouldReturnRegistrationPage() throws Exception {
        this.mockMvc.perform(get("/registration"))
                .andExpect(status().isOk())
                .andExpect(view().name(REGISTRATION_PAGE));
    }


    @Test
    void registerNewUserWhenUserValidAndNotExistShouldRedirectToLoginPage() throws Exception {
        this.mockMvc.perform(post("/registration"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect: /login"));
    }

    //?????Why????
    @Test
    void registerNewUserWhenUserNotValidAndExistShouldRedirectToLoginPage() throws Exception {
        BindingResult errors = mock(BindingResult.class);
        doNothing().when(userDTOValidator).validate(any(), any());
        when(userService.findUserByLogin(anyString())).thenReturn(userDTO1);
        when(errors.hasErrors()).thenReturn(Boolean.TRUE);
        this.mockMvc.perform(post("/registration"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect: /login"));
    }
}