package com.final_ptoject.library_spring.validators;

import com.final_ptoject.library_spring.dto.BookOrderDTO;
import com.final_ptoject.library_spring.entities.BookOrder;
import com.final_ptoject.library_spring.entities.OrderStatus;
import com.final_ptoject.library_spring.entities.OrderType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BookOrderDTOValidatorTest {
    private BookOrderDTO bookOrderDTO;
    private final BookOrderDTOValidator validator = new BookOrderDTOValidator();
    private BindingResult errors;

    @BeforeEach
    void setUp() {
        bookOrderDTO = BookOrderDTO
                .builder()
                .id(1L)
                .orderDate(LocalDateTime.now())
                .openDate(LocalDate.now())
                .closeDate(LocalDate.now())
                .oldCloseDate(LocalDate.now())
                .orderType(new OrderType(1,"reading room"))
                .orderStatus(new OrderStatus(2, "ready"))
                .oldOrderStatusId(2)
                .build();
        errors = new BeanPropertyBindingResult(bookOrderDTO,"bookOrderDTO");
    }

    @Test
    void supportsWhenValidatorSupportsClassThenReturnTrue() {
        assertTrue(validator.supports(BookOrderDTO.class));
    }

    @Test
    void supportsWhenValidatorNotSupportsClassThenReturnFalse() {
        assertFalse(validator.supports(BookOrder.class));
    }

    @Test
    void validateWhenTitleValidThenErrorsIsEmpty() {
        validator.validate(bookOrderDTO, errors);
        assertFalse(errors.hasErrors());
    }

    @Test
    void validateWhenOrderStatusChangesReversThenErrorsHasOrderStatusOrderBackErrorCode() {
        bookOrderDTO.setOldOrderStatusId(3);
        validator.validate(bookOrderDTO, errors);
        assertTrue(errors.hasErrors());
        assertEquals("orderStatus.orderBack", errors.getAllErrors().get(0).getCode());
    }

    @Test
    void validateWhenOrderStatusChangesNotConsecutivelyThenErrorsHasOrderStatusOrderForwardErrorCode() {
        bookOrderDTO.getOrderStatus().setId(4);
        validator.validate(bookOrderDTO, errors);
        assertTrue(errors.hasErrors());
        assertEquals("orderStatus.orderForward", errors.getAllErrors().get(0).getCode());
    }

    @Test
    void validateWhenOrderStatusIdGreater2AndCloseDateIsBeforeOpenDateThenErrorsHasCloseDateBeforeErrorCode() {
        bookOrderDTO.getOrderStatus().setId(3);
        bookOrderDTO.setCloseDate(LocalDate.now().minusDays(1));
        validator.validate(bookOrderDTO, errors);
        assertTrue(errors.hasErrors());
        assertEquals("closeDate.before", errors.getAllErrors().get(0).getCode());
    }

    @Test
    void validateWhenOrderStatusIdGreater2AndCloseDateIsBeforeOldCloseThenErrorsHasCloseDateAfterErrorCode() {
        bookOrderDTO.getOrderStatus().setId(3);
        bookOrderDTO.setOldCloseDate(LocalDate.now().plusDays(1));
        validator.validate(bookOrderDTO, errors);
        assertTrue(errors.hasErrors());
        assertEquals("closeDate.after", errors.getAllErrors().get(0).getCode());
    }

    @Test
    void validateWhenOrderStatusIdGreater2AndOrderTypeIdIs1AndCloseDateNotEqualOpenDateThenErrorsHasCloseDateReadingRoomErrorCode() {
        bookOrderDTO.getOrderStatus().setId(3);
        bookOrderDTO.setCloseDate(LocalDate.now().plusDays(1));
        validator.validate(bookOrderDTO, errors);
        assertTrue(errors.hasErrors());
        assertEquals("closeDate.readingRoom", errors.getAllErrors().get(0).getCode());
    }
}