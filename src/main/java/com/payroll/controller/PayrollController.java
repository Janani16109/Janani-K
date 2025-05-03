package com.payroll.controller;

import com.payroll.service.UserService;
import com.payroll.entity.User;
import java.util.Scanner;

public class PayrollController {
    private final UserService userService;
    private final EmployeeController employeeController;
    private final Scanner scanner;
    private User currentUser;

    public PayrollController(UserService userService, EmployeeController employeeController) {
        this.userService = userService;
        this.employeeController = employeeController;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            if (currentUser == null) {
                showLoginMenu();
            } else {
                showMainMenu();
            }
        }
    }

    private void showLoginMenu() {
        System.out.println("\n=== Payroll System Login ===");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        currentUser = userService.authenticate(username, password);
        if (currentUser == null) {
            System.out.println("Invalid credentials!");
        }
    }

    private void showMainMenu() {
        System.out.println("\n=== Payroll System Menu ===");
        System.out.println("1. Add New Employee");
        System.out.println("2. View Employee");
        System.out.println("3. Calculate Salary");
        System.out.println("4. Generate Tax Report");
        System.out.println("5. Manage Benefits");
        System.out.println("6. Exit");
        System.out.print("Choose an option: ");

        String choice = scanner.nextLine();
        switch (choice) {
            case "1":
                if ("ADMIN".equals(currentUser.getRole()) || "HR".equals(currentUser.getRole())) {
                    employeeController.addNewEmployee();
                } else {
                    System.out.println("Access denied!");
                }
                break;
            case "2":
                employeeController.viewEmployee();
                break;
            case "3":
                calculateSalary();
                break;
            case "4":
                generateTaxReport();
                break;
            case "5":
                if ("ADMIN".equals(currentUser.getRole()) || "HR".equals(currentUser.getRole())) {
                    System.out.print("Enter Employee ID: ");
                    Long employeeId = Long.parseLong(scanner.nextLine());
                    employeeController.manageBenefits(employeeId);
                } else {
                    System.out.println("Access denied!");
                }
                break;
            case "6":
                System.out.println("Thank you for using the Payroll System!");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid option!");
                currentUser = null;
                System.out.println("Logged out successfully!");

        }
    }

    private void calculateSalary() {
        System.out.print("\nEnter Employee ID: ");
        Long employeeId = Long.parseLong(scanner.nextLine());

        System.out.print("Enter Month (1-12): ");
        int month = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter Year: ");
        int year = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter Overtime Hours: ");
        double overtimeHours = Double.parseDouble(scanner.nextLine());

        System.out.print("Enter Bonus Amount: ");
        double bonus = Double.parseDouble(scanner.nextLine());

        try {
            employeeController.calculateAndSaveSalary(employeeId, month, year, overtimeHours, bonus);
            System.out.println("Salary calculated and saved successfully!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void generateTaxReport() {
        System.out.print("\nEnter Employee ID: ");
        Long employeeId = Long.parseLong(scanner.nextLine());

        System.out.print("Enter Tax Year: ");
        String taxYear = scanner.nextLine();

        try {
            employeeController.generateTaxReport(employeeId, taxYear);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}