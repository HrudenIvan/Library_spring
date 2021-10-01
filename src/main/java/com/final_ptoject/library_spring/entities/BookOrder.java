package com.final_ptoject.library_spring.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * BookOrder entity. Refers to table "users_books"
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users_books")
public class BookOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;
    @Column(name = "open_date")
    private LocalDate openDate;
    @Column(name = "close_date")
    private LocalDate closeDate;
    @Column(name = "return_date")
    private LocalDate returnDate;
    @ManyToOne
    @JoinColumn(name = "order_types_id", referencedColumnName = "id", nullable = false)
    private OrderType orderType;
    @ManyToOne
    @JoinColumn(name = "order_statuses_id", referencedColumnName = "id", nullable = false)
    private OrderStatus orderStatus;
    @ManyToOne
    @JoinColumn(name = "users_id", referencedColumnName = "id", nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "books_id", referencedColumnName = "id", nullable = false)
    private Book book;
}
