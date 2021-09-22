package com.final_ptoject.library_spring.dto;

import com.final_ptoject.library_spring.entities.Author;
import com.final_ptoject.library_spring.entities.Publisher;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {
    private long id;
    private String title;
    private int quantity;
    private int quantityOld;
    private int available;
    private int releaseDate;
    private Author author;
    private Publisher publisher;
}
