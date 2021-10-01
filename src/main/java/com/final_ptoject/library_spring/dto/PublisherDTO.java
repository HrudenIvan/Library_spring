package com.final_ptoject.library_spring.dto;

import com.final_ptoject.library_spring.entities.Publisher;
import lombok.*;

/**
 * DTO class for entity {@link Publisher}
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublisherDTO {
    private long id;
    private String name;
}
