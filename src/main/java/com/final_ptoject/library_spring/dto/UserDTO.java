package com.final_ptoject.library_spring.dto;

import com.final_ptoject.library_spring.entities.UserType;
import lombok.*;

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
    private double penalty = 0.0;
    private boolean blocked = false;
    private String password;
    private String passwordConfirm;
    private UserType userType;

}
