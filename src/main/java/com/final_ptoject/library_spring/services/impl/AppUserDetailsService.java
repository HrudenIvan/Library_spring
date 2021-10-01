package com.final_ptoject.library_spring.services.impl;

import com.final_ptoject.library_spring.entities.AppUserDetails;
import com.final_ptoject.library_spring.entities.User;
import com.final_ptoject.library_spring.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Custom implementation of {@link UserDetailsService}
 */
@NoArgsConstructor
@AllArgsConstructor(onConstructor_ = {@Autowired})
@Service
public class AppUserDetailsService implements UserDetailsService {
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findUserByLogin(username);
        return user.map(AppUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("No such user ass " + username));
    }
}
