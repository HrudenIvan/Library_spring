package com.final_ptoject.library_spring.services;

import com.final_ptoject.library_spring.dto.BookOrderDTO;
import com.final_ptoject.library_spring.entities.BookOrder;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Interface for {@link BookOrder} service
 */
public interface BookOrderService {

    BookOrderDTO saveBookOrder(BookOrderDTO bookOrderDTO);

    BookOrderDTO updateBookOrder(Long id, BookOrderDTO bookOrderDTO);

    List<BookOrderDTO> findNewBookOrders();

    List<BookOrderDTO> findOpenBookOrders();

    List<BookOrderDTO> findUsersOngoingOrders(Long userId);

    BookOrderDTO findBookOrderById(Long id);

    List<BookOrderDTO> findUserOpenOrders(Long id);

    Page<BookOrder> findNewBookOrdersPageable(Integer page, Integer size);

    Page<BookOrder> findUserOpenOrdersPageable(Long id, Integer page, Integer size);
}
