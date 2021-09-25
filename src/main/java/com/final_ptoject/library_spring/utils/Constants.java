package com.final_ptoject.library_spring.utils;

public class Constants {

    private Constants() {
    }

    //Admin role
        //Users
    public static final String ADMIN_PAGE = "/administrator/admin_page";
    public static final String ADMIN_USERS_ALL_PAGE = "/administrator/all_users";
    public static final String ADMIN_USERS_ALL_REDIRECT = "redirect:/administrator/users";
    public static final String ADMIN_USER_CREATE_PAGE = "/administrator/create_user";
    public static final String ADMIN_USER_EDIT_PAGE = "/administrator/edit_user";
        //Publishers
    public static final String ADMIN_PUBLISHERS_ALL_PAGE = "/administrator/all_publishers";
    public static final String ADMIN_PUBLISHER_CREATE_PAGE = "/administrator/create_publisher";
    public static final String ADMIN_PUBLISHERS_ALL_REDIRECT = "redirect:/administrator/publishers";
    public static final String ADMIN_PUBLISHER_EDIT_PAGE = "/administrator/edit_publisher";
        //Authors
    public static final String ADMIN_AUTHORS_ALL_PAGE = "/administrator/all_authors";
    public static final String ADMIN_AUTHORS_ALL_REDIRECT = "redirect:/administrator/authors";
    public static final String ADMIN_AUTHOR_CREATE_PAGE = "/administrator/create_author";
    public static final String ADMIN_AUTHOR_EDIT_PAGE = "/administrator/edit_author";
        //Books
    public static final String ADMIN_BOOKS_ALL_PAGE = "/administrator/all_books";
    public static final String ADMIN_BOOKS_ALL_REDIRECT = "redirect:/administrator/books";
    public static final String ADMIN_BOOKS_CREATE_PAGE = "/administrator/create_book";
    public static final String ADMIN_BOOKS_EDIT_PAGE = "/administrator/edit_book";

    //Guest role
    public static final String INDEX_PAGE = "/index";
    public static final String LOGIN_PAGE = "/login";
    public static final String REGISTRATION_PAGE = "/registration";

    //Librarian role
    public static final String LIBRARIAN_PAGE = "/librarian/librarian_page";
    public static final String LIBRARIAN_PAGE_REDIRECT = "redirect:/librarian";
    public static final String LIBRARIAN_ORDER_EDIT_PAGE = "/librarian/edit_book_order";
    public static final String LIBRARIAN_SUBSCRIPTION_PAGE = "/librarian/subscription";

    //User role
    public static final String USER_PAGE = "/user/user_page";
    public static final String USER_BOOKS_PAGE = "/user/books";
    public static final String USER_BOOKS_REDIRECT = "redirect:/user/books";
    public static final String USER_BOOK_ORDER_PAGE = "/user/create_book_order";
}
