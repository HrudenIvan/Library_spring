package com.final_ptoject.library_spring.validators;

import com.final_ptoject.library_spring.dto.BookOrderDTO;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Custom implementation of {@link Validator} interface to validate {@link BookOrderDTO}
 */
@Service
public class BookOrderDTOValidator implements Validator {

    public static final String CLOSE_DATE = "closeDate";

    /**
     * Method to check if given class supports by this validator
     * @param clazz class to be checked
     * @return true if supports, false - otherwise
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return BookOrderDTO.class.isAssignableFrom(clazz);
    }

    /**
     * Validation method of {@link BookOrderDTO} object
     * @param target object to be validated
     * @param errors instance of {@link Errors}
     */
    @Override
    public void validate(Object target, Errors errors) {
        BookOrderDTO bookOrderDTO = (BookOrderDTO) target;
        if (bookOrderDTO.getOrderStatus().getId() > 2) {
            if (bookOrderDTO.getCloseDate().isBefore(bookOrderDTO.getOpenDate())) {
                errors.rejectValue(CLOSE_DATE, "closeDate.before");
            } else if (bookOrderDTO.getCloseDate().isBefore(bookOrderDTO.getOldCloseDate())) {
                errors.rejectValue(CLOSE_DATE, "closeDate.after");
            } else if (!bookOrderDTO.getCloseDate().isEqual(bookOrderDTO.getOpenDate()) &&
                    bookOrderDTO.getOrderType().getId() == 1) {
                errors.rejectValue(CLOSE_DATE, "closeDate.readingRoom");
            }
        }
        if (bookOrderDTO.getOrderStatus().getId() - bookOrderDTO.getOldOrderStatusId() < 0) {
            errors.rejectValue("orderStatus", "orderStatus.orderBack");
        } else if (bookOrderDTO.getOrderStatus().getId() - bookOrderDTO.getOldOrderStatusId() > 1) {
            errors.rejectValue("orderStatus", "orderStatus.orderForward");
        }
    }
}
