package com.final_ptoject.library_spring.repositories;

import com.final_ptoject.library_spring.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByLogin(String login);

    @Query("SELECT DISTINCT u FROM User u JOIN BookOrder bo ON u.id = bo.user.id WHERE bo.orderStatus.id = 2 OR bo.orderStatus.id = 3")
    List<User> getUsersWithOpenOrders();

    @Modifying
    @Query("UPDATE User u SET u.penalty = u.penalty + ?2 WHERE u.id = ?1")
    void updateUserPenalty(Long id, double penalty);
}
