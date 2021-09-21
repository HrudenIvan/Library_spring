package com.final_ptoject.library_spring.services;

import com.final_ptoject.library_spring.dto.UserDTO;
import com.final_ptoject.library_spring.entities.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();

    User saveUser(UserDTO userDTO);

    User findUserById(Long id);

    User updateUser(Long id, UserDTO userDTO);

    void deleteUserById(Long id);
}
