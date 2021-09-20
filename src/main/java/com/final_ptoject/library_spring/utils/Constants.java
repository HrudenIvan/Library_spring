package com.final_ptoject.library_spring.utils;

public class Constants {

    private Constants() {
    }

    //Admin paths
        //Users
    public static final String ADMIN_ALL_USERS_PAGE = "/administrator/all_users";
    public static final String ADMIN_ALL_USERS_REDIRECT = "redirect:/administrator/users";
    public static final String ADMIN_CREATE_USER_PAGE = "/administrator/create_user";
    public static final String ADMIN_EDIT_USER_PAGE = "/administrator/edit_user";
        //Publishers
    public static final String ADMIN_ALL_PUBLISHERS_PAGE = "/administrator/all_publishers";
    public static final String ADMIN_CREATE_PUBLISHER_PAGE = "/administrator/create_publisher";
    public static final String ADMIN_ALL_PUBLISHERS_REDIRECT = "redirect:/administrator/publishers";
    public static final String ADMIN_EDIT_PUBLISHER_PAGE = "/administrator/edit_publisher";
}
