package com.payroll.entity;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role; // ADMIN, HR, EMPLOYEE

    @OneToOne
    @JoinColumn(name = "employee_id")
    private Employee employee; // For employee users

    @Column(nullable = false)
    private boolean active = true;
}