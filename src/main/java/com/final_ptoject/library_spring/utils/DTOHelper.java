package com.final_ptoject.library_spring.utils;

import com.final_ptoject.library_spring.dto.*;
import com.final_ptoject.library_spring.entities.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class that helps with mapping entities to its DTOs
 */
public class DTOHelper {

    /**
     * Secure constructor, to prevent instantiating this class
     */
    private DTOHelper() {
    }

    /**
     * Maps {@link BookOrder} to {@link BookOrderDTO}
     * @param bookOrder {@link BookOrder} to be mapped
     * @return {@link BookOrderDTO}
     */
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

    /**
     * Maps {@link Book} to {@link BookDTO}
     * @param book {@link Book} to be mapped
     * @return {@link BookDTO}
     */
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

    /**
     * Maps {@link Author} to {@link AuthorDTO}
     * @param author {@link Author} to be mapped
     * @return {@link AuthorDTO}
     */
    public static AuthorDTO toDTO(Author author) {
        return AuthorDTO
                .builder()
                .id(author.getId())
                .firstName(author.getFirstName())
                .lastName(author.getLastName())
                .build();
    }

    /**
     * Maps {@link Publisher} to {@link PublisherDTO}
     * @param publisher {@link Publisher} to be mapped
     * @return {@link PublisherDTO}
     */
    public static PublisherDTO toDTO(Publisher publisher) {
        return PublisherDTO
                .builder()
                .id(publisher.getId())
                .name(publisher.getName())
                .build();
    }

    /**
     * Maps {@link User} to {@link UserDTO}
     * @param user {@link User} to be mapped
     * @return {@link UserDTO}
     */
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

    /**
     * Maps list of {@link User}s to list of {@link UserDTO}s
     * @param users list of {@link User}s to be mapped
     * @return list of {@link UserDTO}s
     */
    public static List<UserDTO> userListToDTO(List<User> users) {
        return users.stream().map(DTOHelper::toDTO).collect(Collectors.toList());
    }

    /**
     * Maps list of {@link Publisher}s to list of {@link PublisherDTO}s
     * @param publishers list of {@link Publisher}s to be mapped
     * @return list of {@link PublisherDTO}s
     */
    public static List<PublisherDTO> publisherListToDTO(List<Publisher> publishers) {
        return publishers.stream().map(DTOHelper::toDTO).collect(Collectors.toList());
    }

    /**
     * Maps list of {@link Author}s to list of {@link AuthorDTO}s
     * @param authors list of {@link Author}s to be mapped
     * @return list of {@link AuthorDTO}s
     */
    public static List<AuthorDTO> authorListToDTO(List<Author> authors) {
        return authors.stream().map(DTOHelper::toDTO).collect(Collectors.toList());
    }

    /**
     * Maps list of {@link Book}s to list of {@link BookDTO}s
     * @param books list of {@link Book}s to be mapped
     * @return list of {@link BookDTO}s
     */
    public static List<BookDTO> bookListToDTO(List<Book> books) {
        return books.stream().map(DTOHelper::toDTO).collect(Collectors.toList());
    }

    /**
     * Maps list of {@link BookOrder}s to list of {@link BookOrderDTO}s
     * @param bookOrders list of {@link BookOrder}s to be mapped
     * @return list of {@link BookOrderDTO}s
     */
    public static List<BookOrderDTO> bookOrderListToDTO(List<BookOrder> bookOrders) {
        return bookOrders.stream().map(DTOHelper::toDTO).collect(Collectors.toList());
    }
}
