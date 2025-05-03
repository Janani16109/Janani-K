package com.payroll.controller;

import com.payroll.entity.Employee;
import com.payroll.entity.Salary;
import com.payroll.entity.Tax;
import com.payroll.service.EmployeeService;
import com.payroll.service.TaxService;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class EmployeeController {
    private final EmployeeService employeeService;
    private final TaxService taxService;
    private final Scanner scanner;

    public EmployeeController(EmployeeService employeeService, TaxService taxService) {
        this.employeeService = employeeService;
        this.taxService = taxService;
        this.scanner = new Scanner(System.in);
    }

    public void addNewEmployee() {
        System.out.println("\n=== Add New Employee ===");
        
        Employee employee = new Employee();
        
        System.out.print("Enter First Name: ");
        employee.setFirstName(scanner.nextLine());
        
        System.out.print("Enter Last Name: ");
        employee.setLastName(scanner.nextLine());
        
        System.out.print("Enter Email: ");
        employee.setEmail(scanner.nextLine());
        
        System.out.print("Enter Department: ");
        employee.setDepartment(scanner.nextLine());
        
        System.out.print("Enter Designation: ");
        employee.setDesignation(scanner.nextLine());
        
        System.out.print("Enter Basic Salary: ");
        employee.setBasicSalary(Double.parseDouble(scanner.nextLine()));
        
        employee.setJoiningDate(LocalDate.now());
        
        try {
            employeeService.createEmployee(employee);
            System.out.println("Employee added successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void viewEmployee() {
        System.out.print("\nEnter Employee ID: ");
        Long id = Long.parseLong(scanner.nextLine());
        
        Employee employee = employeeService.getEmployeeById(id);
        if (employee != null) {
            System.out.println("\n=== Employee Details ===");
            System.out.println("ID: " + employee.getId());
            System.out.println("Name: " + employee.getFirstName() + " " + employee.getLastName());
            System.out.println("Email: " + employee.getEmail());
            System.out.println("Department: " + employee.getDepartment());
            System.out.println("Designation: " + employee.getDesignation());
            System.out.println("Basic Salary: " + employee.getBasicSalary());
            System.out.println("Joining Date: " + employee.getJoiningDate());
        } else {
            System.out.println("Employee not found!");
        }
    }

    public void calculateAndSaveSalary(Long employeeId, int month, int year, double overtimeHours, double bonus) {
        Employee employee = employeeService.getEmployeeById(employeeId);
        if (employee == null) {
            throw new IllegalArgumentException("Employee not found!");
        }

        Salary salary = new Salary();
        salary.setEmployee(employee);
        salary.setMonth(String.format("%02d", month));
        salary.setYear(year);
        salary.setPaymentDate(LocalDate.now());
        
        // Basic salary from employee record
        salary.setBasicSalary(employee.getBasicSalary());
        
        // Calculate overtime (assuming 1.5 times hourly rate)
        double hourlyRate = employee.getBasicSalary() / 176; // Assuming 22 working days, 8 hours per day
        salary.setOvertime(overtimeHours * hourlyRate * 1.5);
        
        // Set bonus
        salary.setBonus(bonus);
        
        // Calculate deductions (assuming 12% for PF and other deductions)
        double deductions = employee.getBasicSalary() * 0.12;
        salary.setDeductions(deductions);
        
        // Calculate net salary
        double netSalary = salary.getBasicSalary() + salary.getOvertime() + salary.getBonus() - salary.getDeductions();
        salary.setNetSalary(netSalary);

        // Save salary using employee service
        employeeService.saveSalary(salary);

        // Display salary details
        System.out.println("\n=== Salary Details ===");
        System.out.println("Employee: " + employee.getFirstName() + " " + employee.getLastName());
        System.out.println("Month/Year: " + salary.getMonth() + "/" + salary.getYear());
        System.out.println("Basic Salary: " + salary.getBasicSalary());
        System.out.println("Overtime: " + salary.getOvertime());
        System.out.println("Bonus: " + salary.getBonus());
        System.out.println("Deductions: " + salary.getDeductions());
        System.out.println("Net Salary: " + salary.getNetSalary());
    }

    public void generateTaxReport(Long employeeId, String taxYear) {
        Employee employee = employeeService.getEmployeeById(employeeId);
        if (employee == null) {
            throw new IllegalArgumentException("Employee not found!");
        }
    
        // Calculate total earnings for the year
        double totalEarnings = employee.getBasicSalary() * 12; // Basic annual salary
        double totalBonus = 0;
        double totalOvertime = 0;
    
        // Get all salaries for the year
        List<Salary> yearSalaries = employeeService.getEmployeeSalariesForYear(employeeId, Integer.parseInt(taxYear));
        for (Salary salary : yearSalaries) {
            totalBonus += salary.getBonus();
            totalOvertime += salary.getOvertime();
        }
    
        totalEarnings += totalBonus + totalOvertime;
    
        // Calculate tax using tax service
        Tax tax = taxService.calculateTax(employee, totalEarnings, taxYear);
    
        // Display tax report
        System.out.println("\n=== Tax Report ===");
        System.out.println("Employee: " + employee.getFirstName() + " " + employee.getLastName());
        System.out.println("Tax Year: " + tax.getTaxYear());
        System.out.println("Total Earnings: " + totalEarnings);
        System.out.println("Standard Deduction: " + tax.getStandardDeduction());
        System.out.println("Taxable Income: " + tax.getTaxableIncome());
        System.out.println("Professional Tax: " + tax.getProfessionalTax());
        System.out.println("Income Tax: " + tax.getIncomeTax());
        System.out.println("Total Tax Amount: " + tax.getTotalTaxAmount());
    }
}