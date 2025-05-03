package com.payroll.service;

import com.payroll.entity.Tax;
import com.payroll.entity.Employee;
import com.payroll.repository.TaxRepository;

import java.util.List;

import org.hibernate.Session;

public class TaxService {
    private final TaxRepository taxRepository;

    public TaxService(TaxRepository taxRepository) {
        this.taxRepository = taxRepository;
    }

    public Tax calculateTax(Employee employee, double annualIncome, String taxYear) {
        Tax tax = new Tax();
        tax.setEmployee(employee);
        tax.setTaxYear(taxYear);

        // Standard deduction for all employees
        double standardDeduction = 50000.0;
        tax.setStandardDeduction(standardDeduction);

        // Calculate taxable income after standard deduction
        double taxableIncome = annualIncome - standardDeduction;
        tax.setTaxableIncome(taxableIncome);

        // Professional Tax (fixed amount per year)
        double professionalTax = 2400.0; // ₹200 per month
        tax.setProfessionalTax(professionalTax);

        // Calculate Income Tax based on slabs
        double incomeTax = 0.0;
        if (taxableIncome <= 250000) {
            incomeTax = 0.0;
        } else if (taxableIncome <= 500000) {
            incomeTax = (taxableIncome - 250000) * 0.05;
        } else if (taxableIncome <= 1000000) {
            incomeTax = 12500 + (taxableIncome - 500000) * 0.20;
        } else {
            incomeTax = 112500 + (taxableIncome - 1000000) * 0.30;
        }

        tax.setIncomeTax(incomeTax);
        tax.setTotalTaxAmount(incomeTax + professionalTax);

        // Save the tax calculation
        return taxRepository.save(tax);
    }

    public Tax getTaxById(Long id) {
        return taxRepository.findById(id);
    }

    public List<Tax> getEmployeeTaxHistory(Long employeeId) {
        return taxRepository.findByEmployeeId(employeeId);
    }
}