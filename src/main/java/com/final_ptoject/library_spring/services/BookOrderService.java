package com.final_ptoject.library_spring.services;

import com.final_ptoject.library_spring.dto.BookOrderDTO;

import java.util.List;

public interface BookOrderService {

    BookOrderDTO saveBookOrder(BookOrderDTO bookOrderDTO);

    BookOrderDTO updateBookOrder(Long id, BookOrderDTO bookOrderDTO);

    List<BookOrderDTO> findNewBookOrders();

    List<BookOrderDTO> findOpenBookOrders();

    List<BookOrderDTO> findUsersOngoingOrders(Long userId);

    BookOrderDTO findBookOrderById(Long id);

    List<BookOrderDTO> findUserOpenOrders(Long id);
}
