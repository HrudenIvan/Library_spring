package com.final_ptoject.library_spring.repositories;

import com.final_ptoject.library_spring.entities.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for {@link UserType}
 */
@Repository
public interface UserTypeRepository extends JpaRepository<UserType, Integer> {
}
