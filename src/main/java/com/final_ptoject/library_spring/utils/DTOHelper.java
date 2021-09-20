package com.final_ptoject.library_spring.utils;

import com.final_ptoject.library_spring.dto.UserDTO;
import com.final_ptoject.library_spring.entities.User;

import java.util.ArrayList;
import java.util.List;

public class DTOHelper {

    private DTOHelper() {
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

    public static List<UserDTO> toDTO(List<User> users) {
        List<UserDTO> userDTOS = new ArrayList<>();
        for (User user : users) {
            userDTOS.add(toDTO(user));
        }

        return userDTOS;
    }

}
