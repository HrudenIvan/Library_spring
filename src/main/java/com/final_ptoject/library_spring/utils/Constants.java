package com.final_ptoject.library_spring.utils;

public class Constants {

    private Constants() {
    }

    //Admin paths
    //Users
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
}
