package com.final_ptoject.library_spring.utils;

import lombok.Getter;
import lombok.Setter;

public class Pagination {
    private Pagination() {}

    @Getter
    @Setter
    public static class BookPaginationParam {
        private String sortBy;
        private String sortOrder;
        private String title;
        private String authorLastName;
        private String authorFirstName;

        public BookPaginationParam() {
            sortBy="title";
            sortOrder="asc";
            title="";
            authorFirstName="";
            authorLastName="";
        }

        public String buildPaginationNavigation(int currentPage, int totalPages) {
            if (totalPages == 1) {
                return "";
            }
            StringBuilder requestParams = new StringBuilder();
            requestParams
                    .append("/?sortBy=")
                    .append(sortBy)
                    .append("&sortOrder=")
                    .append(sortOrder)
                    .append("&title=")
                    .append(title)
                    .append("&authorLastName=")
                    .append(authorLastName)
                    .append("&authorFirstName=")
                    .append(authorFirstName)
                    .append("&page=");
            StringBuilder paginationNavigation = new StringBuilder();
            paginationNavigation.append("<nav><ul class=\"pagination pagination-lg\">");
            for (int i = 0; i < totalPages; i++) {
                if (i == currentPage - 1) {
                    paginationNavigation
                            .append("<li class=\"page-item active\"><span class=\"page-link\">")
                            .append(i+1)
                            .append("</span></li>");
                } else {
                    paginationNavigation
                            .append("<li class=\"page-item\"><a class=\"page-link\" href=\"")
                            .append(requestParams)
                            .append(i+1)
                            .append("\">")
                            .append(i+1)
                            .append("</a></li>");
                }
            }
            paginationNavigation
                    .append("</ul></nav>");
            return paginationNavigation.toString();
        }
    }

}
