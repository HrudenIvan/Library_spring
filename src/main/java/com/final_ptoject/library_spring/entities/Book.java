package com.final_ptoject.library_spring.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Book entity. Refers to table "books"
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "title", nullable = false, length = 100)
    private String title;
    @Column(name = "quantity", nullable = false)
    private int quantity;
    @Column(name = "available", nullable = false)
    private int available;
    @Column(name = "release_date", nullable = false)
    private int releaseDate;
    @ManyToOne
    @JoinColumn(name = "authors_id", referencedColumnName = "id", nullable = false)
    private Author author;
    @ManyToOne
    @JoinColumn(name = "publishers_id", referencedColumnName = "id", nullable = false)
    private Publisher publisher;
}
