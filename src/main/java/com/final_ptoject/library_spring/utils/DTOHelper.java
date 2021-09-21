package com.final_ptoject.library_spring.utils;

import com.final_ptoject.library_spring.dto.AuthorDTO;
import com.final_ptoject.library_spring.dto.PublisherDTO;
import com.final_ptoject.library_spring.dto.UserDTO;
import com.final_ptoject.library_spring.entities.Author;
import com.final_ptoject.library_spring.entities.Publisher;
import com.final_ptoject.library_spring.entities.User;

import java.util.List;
import java.util.stream.Collectors;

public class DTOHelper {

    private DTOHelper() {
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

}
