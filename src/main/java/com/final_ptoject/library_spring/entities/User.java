package com.final_ptoject.library_spring.entities;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "login", nullable = false, length = 20, unique = true)
    private String login;
    @Column(name = "first_name", nullable = false, length = 30)
    private String firstName;
    @Column(name = "last_name", nullable = false, length = 30)
    private String lastName;
    @Column(name = "penalty", nullable = false)
    private double penalty;
    @Column(name = "is_blocked", nullable = false)
    private boolean blocked;
    @Column(name = "password", nullable = false, length = 60)
    private String password;
    @ManyToOne
    @JoinColumn(name = "user_types_id", referencedColumnName = "id", nullable = false)
    private UserType userType;
}
