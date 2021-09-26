package com.final_ptoject.library_spring.repositories;

import com.final_ptoject.library_spring.entities.BookOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookOrderRepository extends JpaRepository<BookOrder, Long> {

    @Query("SELECT bo FROM BookOrder bo WHERE bo.user.id = ?1 AND bo.orderStatus.id <> 4")
    List<BookOrder> getUsersOngoingOrders(Long id);

    @Query("SELECT bo FROM  BookOrder bo WHERE bo.orderStatus.id = 1")
    List<BookOrder> getNewBookOrders();

    @Query("SELECT bo FROM  BookOrder bo WHERE bo.orderStatus.id = 2 or bo.orderStatus.id = 3")
    List<BookOrder> getOpenBookOrders();

    @Query("SELECT bo FROM  BookOrder bo WHERE (bo.orderStatus.id = 2 or bo.orderStatus.id = 3) AND bo.user.id = ?1")
    List<BookOrder> getUsersOpenBookOrders(Long id);

    @Query("SELECT bo FROM  BookOrder bo WHERE bo.orderStatus.id = 1")
    Page<BookOrder> findNewBookOrdersPageable(Pageable pageable);

    @Query("SELECT bo FROM  BookOrder bo WHERE (bo.orderStatus.id = 2 or bo.orderStatus.id = 3) AND bo.user.id = ?1")
    Page<BookOrder> findUserOpenOrdersPageable(Long id, Pageable pageable);
}
