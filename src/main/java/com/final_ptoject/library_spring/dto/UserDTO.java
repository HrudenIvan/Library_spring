package com.final_ptoject.library_spring.dto;

import com.final_ptoject.library_spring.entities.User;
import com.final_ptoject.library_spring.entities.UserType;
import lombok.*;

/**
 * DTO class for entity {@link User}
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDTO {
    private long id;
    private String login;
    private String firstName;
    private String lastName;
    private double penalty;
    private boolean blocked;
    private String password;
    private String passwordConfirm;
    private UserType userType;

}
