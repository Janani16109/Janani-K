package com.payroll;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import com.payroll.controller.EmployeeController;
import com.payroll.controller.PayrollController;
import com.payroll.service.EmployeeService;
import com.payroll.service.UserService;
import com.payroll.service.TaxService;
import com.payroll.repository.UserRepository;
import com.payroll.repository.TaxRepository;

public class PayrollApplication {
    public static void main(String[] args) {
        // Create the SessionFactory
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure() // configures settings from hibernate.cfg.xml
            .build();
        
        try {
            SessionFactory sessionFactory = new MetadataSources(registry)
                .buildMetadata()
                .buildSessionFactory();

            // Get a session
            Session session = sessionFactory.openSession();

            // Initialize repositories
            UserRepository userRepository = new UserRepository(session);
            TaxRepository taxRepository = new TaxRepository(session);

            // Initialize services
            UserService userService = new UserService(userRepository);
            EmployeeService employeeService = new EmployeeService(session);
            TaxService taxService = new TaxService(taxRepository);

            // Initialize controllers
            EmployeeController employeeController = new EmployeeController(employeeService, taxService);
            PayrollController payrollController = new PayrollController(userService, employeeController);

            // Start the application
            System.out.println("Welcome to Employee Payroll System");
            payrollController.start();

        } catch (Exception e) {
            System.err.println("Error initializing the application: " + e.getMessage());
            e.printStackTrace();
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}