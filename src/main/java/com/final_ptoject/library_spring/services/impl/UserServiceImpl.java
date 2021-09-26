package com.final_ptoject.library_spring.services.impl;

import com.final_ptoject.library_spring.dto.UserDTO;
import com.final_ptoject.library_spring.entities.User;
import com.final_ptoject.library_spring.repositories.UserRepository;
import com.final_ptoject.library_spring.services.UserService;
import com.final_ptoject.library_spring.utils.DTOHelper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.final_ptoject.library_spring.utils.DTOHelper.userListToDTO;

@NoArgsConstructor
@AllArgsConstructor(onConstructor_ = {@Autowired})
@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

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
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
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
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        user.setUserType(userDTO.getUserType());
        return userRepository.save(user);
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserDTO findUserByLogin(String login) {
        return userRepository.findUserByLogin(login).map(DTOHelper::toDTO).orElse(null);
    }

    @Override
    public List<UserDTO> findUsersWithOpenOrders() {
        return userListToDTO(userRepository.getUsersWithOpenOrders());
    }

    @Override
    public Page<User> getAllUsersPageable(Integer page, Integer size) {
        return userRepository.findAll(PageRequest.of(page, size, Sort.by("userType.type")));
    }

    @Override
    public Page<User> findUsersWithOpenOrdersPageable(Integer page, Integer size) {
        return userRepository.getUsersWithOpenOrdersPageable(PageRequest.of(page, size, Sort.by("lastName")));
    }
}
