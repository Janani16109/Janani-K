package com.payroll.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "salaries")
public class Salary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(nullable = false)
    private LocalDate paymentDate;

    @Column(nullable = false)
    private double basicSalary;

    private double overtime;
    private double bonus;
    private double deductions;
    private double taxAmount;
    private double netSalary;

    @Column(nullable = false)
    private String month;

    @Column(nullable = false)
    private int year;
}