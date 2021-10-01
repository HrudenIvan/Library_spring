package com.final_ptoject.library_spring.services;

import com.final_ptoject.library_spring.dto.PublisherDTO;
import com.final_ptoject.library_spring.entities.Publisher;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Interface for {@link Publisher} service
 */
public interface PublisherService {

    List<Publisher> getAllPublishers();

    Publisher savePublisher(PublisherDTO publisherDTO);

    Publisher findPublisherById(Long id);

    Publisher updatePublisher(Long id, PublisherDTO publisherDTO);

    void deletePublisherById(Long id);

    Page<Publisher> getAllPublishersPageable(Integer page, Integer size);
}
