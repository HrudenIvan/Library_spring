package com.final_ptoject.library_spring.repositories;

import com.final_ptoject.library_spring.entities.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for {@link Publisher}
 */
@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Long> {
}
