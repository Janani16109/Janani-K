package com.payroll.entity;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "taxes")
public class Tax {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(nullable = false)
    private String taxYear;

    @Column(nullable = false)
    private double taxableIncome;

    private double standardDeduction;
    private double professionalTax;
    private double incomeTax;
    private double totalTaxAmount;

    @PrePersist
    public void prePersist() {
        // Initialize all tax-related fields to 0.0 if not set
        if (standardDeduction == 0.0) {
            standardDeduction = 50000.0; // Default standard deduction
        }
        if (professionalTax == 0.0) {
            professionalTax = 2400.0; // Default professional tax
        }
        if (incomeTax == 0.0) {
            // Calculate income tax based on taxable income
            if (taxableIncome <= 250000) {
                incomeTax = 0.0;
            } else if (taxableIncome <= 500000) {
                incomeTax = (taxableIncome - 250000) * 0.05;
            } else if (taxableIncome <= 1000000) {
                incomeTax = 12500 + (taxableIncome - 500000) * 0.20;
            } else {
                incomeTax = 112500 + (taxableIncome - 1000000) * 0.30;
            }
        }
        // Calculate total tax amount
        totalTaxAmount = incomeTax + professionalTax;
    }
}