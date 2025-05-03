package com.payroll.entity;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "benefits")
public class Benefits {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    private double healthInsurance;
    private double providentFund;
    private double gratuity;
    private boolean mealCard;
    private boolean transportAllowance;
    private String effectiveDate;
    private boolean active;
}
