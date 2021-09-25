package com.final_ptoject.library_spring.utils;

import com.final_ptoject.library_spring.dto.*;
import com.final_ptoject.library_spring.entities.*;

import java.util.List;
import java.util.stream.Collectors;

public class DTOHelper {

    private DTOHelper() {
    }

    public static BookOrderDTO toDTO(BookOrder bookOrder) {
        BookOrderDTO bookOrderDTO = BookOrderDTO
                .builder()
                .id(bookOrder.getId())
                .orderDate(bookOrder.getOrderDate())
                .openDate(bookOrder.getOpenDate())
                .closeDate(bookOrder.getCloseDate())
                .oldCloseDate(bookOrder.getCloseDate())
                .returnDate(bookOrder.getReturnDate())
                .orderType(bookOrder.getOrderType())
                .orderStatus(bookOrder.getOrderStatus())
                .oldOrderStatusId(bookOrder.getOrderStatus().getId())
                .user(bookOrder.getUser())
                .book(bookOrder.getBook())
                .build();
        bookOrderDTO.calculatePenalty();
        return bookOrderDTO;
    }

    public static BookDTO toDTO(Book book) {
        return BookDTO
                .builder()
                .id(book.getId())
                .title(book.getTitle())
                .quantity(book.getQuantity())
                .quantityOld(book.getQuantity())
                .available(book.getAvailable())
                .releaseDate(book.getReleaseDate())
                .author(book.getAuthor())
                .publisher(book.getPublisher())
                .build();
    }

    public static AuthorDTO toDTO(Author author) {
        return AuthorDTO
                .builder()
                .id(author.getId())
                .firstName(author.getFirstName())
                .lastName(author.getLastName())
                .build();
    }

    public static PublisherDTO toDTO(Publisher publisher) {
        return PublisherDTO
                .builder()
                .id(publisher.getId())
                .name(publisher.getName())
                .build();
    }

    public static UserDTO toDTO(User user) {
        return UserDTO
                .builder()
                .id(user.getId())
                .login(user.getLogin())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .penalty(user.getPenalty())
                .blocked(user.isBlocked())
                .userType(user.getUserType())
                .build();
    }

    public static List<UserDTO> userListToDTO(List<User> users) {
        return users.stream().map(DTOHelper::toDTO).collect(Collectors.toList());
    }

    public static List<PublisherDTO> publisherListToDTO(List<Publisher> publishers) {
        return publishers.stream().map(DTOHelper::toDTO).collect(Collectors.toList());
    }

    public static List<AuthorDTO> authorListToDTO(List<Author> authors) {
        return authors.stream().map(DTOHelper::toDTO).collect(Collectors.toList());
    }

    public static List<BookDTO> bookListToDTO(List<Book> books) {
        return books.stream().map(DTOHelper::toDTO).collect(Collectors.toList());
    }

    public static List<BookOrderDTO> bookOrderListToDTO(List<BookOrder> bookOrders) {
        return bookOrders.stream().map(DTOHelper::toDTO).collect(Collectors.toList());
    }
}
