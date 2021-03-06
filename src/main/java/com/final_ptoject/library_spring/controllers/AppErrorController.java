package com.final_ptoject.library_spring.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

/**
 * Controller to handle errors
 */
@Controller
public class AppErrorController implements ErrorController {
    private static final Logger logger = LoggerFactory.getLogger(AppErrorController.class);

    /**
     * Method to handle errors. Handles request to URL "/error".
     * If HttpStatus is FORBIDDEN, then forwards to "forward:/back",
     * if HttpStatus is NOT_FOUND, then forwards to "/error_404",
     * if HttpStatus is INTERNAL_SERVER_ERROR, then forwards to "/error"
     * @param request {@link HttpServletRequest}
     * @param e {@link Exception}
     * @return view
     */
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Exception e) {
        String path = ERROR_PAGE;
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            if (statusCode == HttpStatus.FORBIDDEN.value()) {
                logger.info("Attempt of unauthorized access");
                path = BACK;
            } else if (statusCode == HttpStatus.NOT_FOUND.value()) {
                logger.info("Attempt of access to non existing resource");
                path = ERROR_404_PAGE;
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                logger.error("Request " + request.getRequestURL() + " raised ", e);
            }
        }
        return path;
    }

    /**
     * Method to handle request "/back". Redirects to user role specific page:
     * for "ADMIN" - "redirect:/administrator", for "LIBRARIAN" - "redirect:/librarian",
     * for "USER" - "redirect:/user/books", guest must be redirected to "redirect:/"
     * @return view
     */
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
