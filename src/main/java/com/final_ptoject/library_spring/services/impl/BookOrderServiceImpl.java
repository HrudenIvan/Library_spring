package com.final_ptoject.library_spring.services.impl;

import com.final_ptoject.library_spring.dto.BookOrderDTO;
import com.final_ptoject.library_spring.entities.BookOrder;
import com.final_ptoject.library_spring.repositories.BookOrderRepository;
import com.final_ptoject.library_spring.repositories.BookRepository;
import com.final_ptoject.library_spring.repositories.UserRepository;
import com.final_ptoject.library_spring.services.AuthorService;
import com.final_ptoject.library_spring.services.BookOrderService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static com.final_ptoject.library_spring.utils.DTOHelper.bookOrderListToDTO;
import static com.final_ptoject.library_spring.utils.DTOHelper.toDTO;

/**
 * Implementation of {@link BookOrderService}
 */
@NoArgsConstructor
@AllArgsConstructor(onConstructor_ = {@Autowired})
@Service
public class BookOrderServiceImpl implements BookOrderService {
    private BookOrderRepository bookOrderRepository;
    private BookRepository bookRepository;
    private UserRepository userRepository;

    @Override
    @Transactional
    public BookOrderDTO saveBookOrder(BookOrderDTO bookOrderDTO) {
        BookOrder bookOrder = new BookOrder();
        bookOrder.setUser(bookOrderDTO.getUser());
        bookOrder.setBook(bookOrderDTO.getBook());
        bookOrder.setOrderDate(bookOrderDTO.getOrderDate());
        bookOrder.setOrderStatus(bookOrderDTO.getOrderStatus());
        bookOrder.setOrderType(bookOrderDTO.getOrderType());
        bookRepository.decrementBookAvailable(bookOrder.getBook().getId());
        return toDTO(bookOrderRepository.save(bookOrder));
    }

    @Transactional
    @Override
    public BookOrderDTO updateBookOrder(Long id, BookOrderDTO bookOrderDTO) {
        BookOrder bookOrder = bookOrderRepository.getById(id);
        if (bookOrderDTO.getOrderStatus().getId() == 3) {
            bookOrder.setOpenDate(bookOrderDTO.getOpenDate());
            bookOrder.setCloseDate(bookOrderDTO.getCloseDate());
        }
        if (bookOrderDTO.getOrderStatus().getId() == 4) {
            bookOrder.setReturnDate(LocalDate.now());
            bookOrderDTO.calculatePenalty();
            userRepository.updateUserPenalty(bookOrder.getUser().getId(), bookOrderDTO.getPenalty());
            bookRepository.incrementBookAvailable(bookOrder.getBook().getId());
        }
        bookOrder.setOrderStatus(bookOrderDTO.getOrderStatus());
        return toDTO(bookOrderRepository.save(bookOrder));
    }

    @Override
    public List<BookOrderDTO> findNewBookOrders() {
        return bookOrderListToDTO(bookOrderRepository.getNewBookOrders());
    }

    @Override
    public List<BookOrderDTO> findOpenBookOrders() {
        return bookOrderListToDTO(bookOrderRepository.getOpenBookOrders());
    }

    @Override
    public List<BookOrderDTO> findUsersOngoingOrders(Long userId) {
        return bookOrderListToDTO(bookOrderRepository.getUsersOngoingOrders(userId));
    }

    @Override
    public BookOrderDTO findBookOrderById(Long id) {
        return toDTO(bookOrderRepository.getById(id));
    }

    @Override
    public List<BookOrderDTO> findUserOpenOrders(Long id) {
        return bookOrderListToDTO(bookOrderRepository.getUsersOpenBookOrders(id));
    }

    @Override
    public Page<BookOrder> findNewBookOrdersPageable(Integer page, Integer size) {
        return bookOrderRepository.findNewBookOrdersPageable(PageRequest.of(page, size, Sort.by("orderDate")));
    }

    @Override
    public Page<BookOrder> findUserOpenOrdersPageable(Long id, Integer page, Integer size) {
        return bookOrderRepository.findUserOpenOrdersPageable(id, PageRequest.of(page, size, Sort.by("closeDate")));
    }
}
