package com.payroll.service;

import com.payroll.entity.Employee;
import com.payroll.entity.Salary;
import com.payroll.entity.Benefits;
import org.hibernate.Session;
import java.time.LocalDate;
import java.util.List;

public class EmployeeService {
    private final Session session;

    public EmployeeService(Session session) {
        this.session = session;
    }

    public Employee createEmployee(Employee employee) {
        if (employee.getFirstName() == null || employee.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("First name cannot be empty");
        }
        if (employee.getEmail() == null || employee.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }

        session.beginTransaction();
        session.save(employee);
        session.getTransaction().commit();
        return employee;
    }

    public Employee updateEmployee(Employee employee) {
        session.beginTransaction();
        session.update(employee);
        session.getTransaction().commit();
        return employee;
    }

    public void deleteEmployee(Long id) {
        session.beginTransaction();
        Employee employee = session.get(Employee.class, id);
        if (employee != null) {
            session.delete(employee);
        }
        session.getTransaction().commit();
    }

    public Employee getEmployeeById(Long id) {
        return session.get(Employee.class, id);
    }

    // Add this method to the EmployeeService class
    public void saveSalary(Salary salary) {
        session.beginTransaction();
        session.save(salary);
        session.getTransaction().commit();
    }

    public List<Salary> getEmployeeSalariesForYear(Long employeeId, int year) {
        return session.createQuery(
            "from Salary s where s.employee.id = :employeeId and s.year = :year", 
            Salary.class)
            .setParameter("employeeId", employeeId)
            .setParameter("year", year)
            .getResultList();
    }
}