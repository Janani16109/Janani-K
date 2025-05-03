package com.payroll.controller;

import com.payroll.entity.Benefits;
import com.payroll.entity.Employee;
import com.payroll.entity.Salary;
import com.payroll.entity.Tax;
import com.payroll.service.BenefitsService; 
import com.payroll.service.EmployeeService;
import com.payroll.service.TaxService;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class EmployeeController {
    private final EmployeeService employeeService;
    private final TaxService taxService;
    private final BenefitsService benefitsService;
    private final Scanner scanner;

    public EmployeeController(EmployeeService employeeService, TaxService taxService, BenefitsService benefitsService) {
        this.employeeService = employeeService;
        this.taxService = taxService;
        this.benefitsService = benefitsService;
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
    
    public void manageBenefits(Long employeeId) {
        Employee employee = employeeService.getEmployeeById(employeeId);
        if (employee == null) {
            System.out.println("Employee not found!");
            return;
        }
    
        Benefits benefits = benefitsService.getBenefitsByEmployeeId(employeeId);
        if (benefits == null) {
            benefits = benefitsService.createBenefits(employee);
            System.out.println("New benefits package created!");
        }
    
        while (true) {
            System.out.println("\n=== Employee Benefits ===");
            System.out.println("1. View Benefits");
            System.out.println("2. Update Health Insurance");
            System.out.println("3. Update Provident Fund");
            System.out.println("4. Update Gratuity");
            System.out.println("5. Toggle Meal Card");
            System.out.println("6. Toggle Transport Allowance");
            System.out.println("7. Back to Main Menu");
            System.out.print("Choose an option: ");
    
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    displayBenefits(benefits);
                    break;
                case "2":
                    System.out.print("Enter new Health Insurance amount: ");
                    double healthInsurance = Double.parseDouble(scanner.nextLine());
                    benefits.setHealthInsurance(healthInsurance);
                    break;
                case "3":
                    System.out.print("Enter new Provident Fund percentage (0-100): ");
                    double pfPercentage = Double.parseDouble(scanner.nextLine());
                    benefits.setProvidentFund(employee.getBasicSalary() * (pfPercentage / 100));
                    break;
                case "4":
                    System.out.print("Enter new Gratuity percentage (0-100): ");
                    double gratuityPercentage = Double.parseDouble(scanner.nextLine());
                    benefits.setGratuity(employee.getBasicSalary() * (gratuityPercentage / 100));
                    break;
                case "5":
                    benefits.setMealCard(!benefits.isMealCard());
                    System.out.println("Meal Card: " + (benefits.isMealCard() ? "Enabled" : "Disabled"));
                    break;
                case "6":
                    benefits.setTransportAllowance(!benefits.isTransportAllowance());
                    System.out.println("Transport Allowance: " + (benefits.isTransportAllowance() ? "Enabled" : "Disabled"));
                    break;
                case "7":
                    benefitsService.updateBenefits(benefits);
                    return;
                default:
                    System.out.println("Invalid option!");
            }
    
            if (!choice.equals("1") && !choice.equals("7")) {
                benefitsService.updateBenefits(benefits);
                System.out.println("Benefits updated successfully!");
            }
        }
    }
    
    private void displayBenefits(Benefits benefits) {
        System.out.println("\n=== Current Benefits ===");
        System.out.println("Health Insurance: " + benefits.getHealthInsurance());
        System.out.println("Provident Fund: " + benefits.getProvidentFund());
        System.out.println("Gratuity: " + benefits.getGratuity());
        System.out.println("Meal Card: " + (benefits.isMealCard() ? "Yes" : "No"));
        System.out.println("Transport Allowance: " + (benefits.isTransportAllowance() ? "Yes" : "No"));
        System.out.println("Effective Date: " + benefits.getEffectiveDate());
    }
}