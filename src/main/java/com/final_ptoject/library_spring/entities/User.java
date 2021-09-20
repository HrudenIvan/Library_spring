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
    @Column(name = "password", nullable = false, length = 32)
    private byte[] password;
    @Column(name = "salt", nullable = false, length = 32)
    private byte[] salt;
    @ManyToOne
    @JoinColumn(name = "user_type_id", referencedColumnName = "id")
    private UserType userType;
}
