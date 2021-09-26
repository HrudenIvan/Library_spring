package com.final_ptoject.library_spring.services.impl;

import com.final_ptoject.library_spring.dto.PublisherDTO;
import com.final_ptoject.library_spring.entities.Publisher;
import com.final_ptoject.library_spring.repositories.PublisherRepository;
import com.final_ptoject.library_spring.services.PublisherService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor(onConstructor_ = {@Autowired})
@Service
public class PublisherServiceImpl implements PublisherService {
    private PublisherRepository publisherRepository;

    public List<Publisher> getAllPublishers() {
        return publisherRepository.findAll();
    }

    @Override
    public Publisher savePublisher(PublisherDTO publisherDTO) {
        Publisher publisher = new Publisher();
        publisher.setName(publisherDTO.getName());
        return publisherRepository.save(publisher);
    }

    @Override
    public Publisher findPublisherById(Long id) {
        return publisherRepository.getById(id);
    }

    @Override
    public Publisher updatePublisher(Long id, PublisherDTO publisherDTO) {
        Publisher publisher = publisherRepository.getById(id);
        publisher.setName(publisherDTO.getName());
        return publisherRepository.save(publisher);
    }

    @Override
    public void deletePublisherById(Long id) {
        publisherRepository.deleteById(id);
    }

    @Override
    public Page<Publisher> getAllPublishersPageable(Integer page, Integer size) {
        return publisherRepository.findAll(PageRequest.of(page, size, Sort.by("name")));
    }
}
