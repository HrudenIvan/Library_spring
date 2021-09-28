package com.final_ptoject.library_spring.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import java.util.Collection;

import static com.final_ptoject.library_spring.utils.Constants.*;

@Controller
public class AppErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        String path = ERROR_PAGE;
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            if (statusCode == HttpStatus.FORBIDDEN.value()) {
                path = BACK;
            }
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                path = ERROR_404_PAGE;
            }
        }
        return path;
    }

    @RequestMapping("/back")
    public String back() {
        String path = INDEX_PAGE_REDIRECT;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal == null) {
            return path;
        }
        Collection<? extends GrantedAuthority> authorities = ((UserDetails) principal).getAuthorities();
        String authority = authorities.toString();
        if (authority.contains("ROLE_ADMIN")) {
            path = ADMIN_PAGE_REDIRECT;
        } else if (authority.contains("ROLE_LIBRARIAN")) {
            path = LIBRARIAN_PAGE_REDIRECT;
        } else if (authority.contains("ROLE_USER")) {
            path = USER_BOOKS_REDIRECT;
        }
        return path;
    }
}
