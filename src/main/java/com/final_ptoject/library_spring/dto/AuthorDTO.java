package com.final_ptoject.library_spring.dto;

import com.final_ptoject.library_spring.entities.Author;
import lombok.*;

/**
 * DTO class for entity {@link Author}
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorDTO {
    private long id;
    private String firstName;
    private String lastName;
}
