package com.final_ptoject.library_spring.services.impl;

import com.final_ptoject.library_spring.dto.UserDTO;
import com.final_ptoject.library_spring.entities.User;
import com.final_ptoject.library_spring.repositories.UserRepository;
import com.final_ptoject.library_spring.repositories.UserTypeRepository;
import com.final_ptoject.library_spring.services.UserService;
import com.final_ptoject.library_spring.utils.Password;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor(onConstructor_ = {@Autowired})
@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User saveUser(UserDTO userDTO) {
        User user = new User();
        user.setLogin(userDTO.getLogin());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setPenalty(userDTO.getPenalty());
        user.setBlocked(userDTO.isBlocked());
        user.setSalt(Password.generateSalt());
        user.setPassword(Password.hash(userDTO.getPassword(), user.getSalt()));
        user.setUserType(userDTO.getUserType());
        return userRepository.save(user);
    }

    @Override
    public User findUserById(Long id) {
        return userRepository.getById(id);
    }

    @Override
    public User updateUser(Long id, UserDTO userDTO) {
        User user = userRepository.getById(id);
        user.setLogin(userDTO.getLogin());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setPenalty(userDTO.getPenalty());
        user.setBlocked(userDTO.isBlocked());
        if (!userDTO.getPassword().isEmpty()) {
            user.setSalt(Password.generateSalt());
            user.setPassword(Password.hash(userDTO.getPassword(), user.getSalt()));
        }
        user.setUserType(userDTO.getUserType());
        return userRepository.save(user);
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }
}
