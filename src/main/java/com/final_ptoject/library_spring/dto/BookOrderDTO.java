package com.final_ptoject.library_spring.dto;

import com.final_ptoject.library_spring.entities.Book;
import com.final_ptoject.library_spring.entities.OrderStatus;
import com.final_ptoject.library_spring.entities.OrderType;
import com.final_ptoject.library_spring.entities.User;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Properties;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookOrderDTO {
    private long id;
    private LocalDateTime orderDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate openDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate closeDate;
    private LocalDate oldCloseDate;
    private LocalDate returnDate;
    private OrderType orderType;
    private OrderStatus orderStatus;
    private int oldOrderStatusId;
    private User user;
    private Book book;
    private double penalty;
    private static final double PENALTY_PER_DAY;

    static {
        double penaltyPerDay=1;
        try {
        Properties properties = new Properties();
        InputStream inputStream =
                BookOrderDTO.class.getClassLoader().getResourceAsStream("application.properties");

            properties.load(inputStream);
            penaltyPerDay = Double.parseDouble(properties.getProperty("penaltyPerDay"));
        } catch (IOException e) {
            // TODO
            e.printStackTrace();
        }
        PENALTY_PER_DAY=penaltyPerDay;
    }

    public void calculatePenalty() {
        if (this.getOrderStatus().getId() < 3) {
            this.penalty = 0;
        } else {
            long days = LocalDate.now().toEpochDay() - this.closeDate.toEpochDay();
            if (days > 0) {
                this.penalty = PENALTY_PER_DAY * days;
            } else {
                this.penalty = 0;
            }
        }
    }
}
