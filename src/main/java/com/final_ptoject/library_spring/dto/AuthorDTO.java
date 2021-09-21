package com.final_ptoject.library_spring.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorDTO {
    private long id=0;
    private String firstName;
    private String lastName;
}
