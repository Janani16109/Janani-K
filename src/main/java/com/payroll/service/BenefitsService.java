package com.payroll.service;

import com.payroll.entity.Benefits;
import com.payroll.entity.Employee;
import org.hibernate.Session;
import java.time.LocalDate;

public class BenefitsService {
    private final Session session;

    public BenefitsService(Session session) {
        this.session = session;
    }    

    public Benefits getBenefitsByEmployeeId(Long employeeId) {
        try {
            return session.createQuery(
                "from Benefits b where b.employee.id = :employeeId and b.active = true",
                Benefits.class)
                .setParameter("employeeId", employeeId)
                .uniqueResult();
        } catch (Exception e) {
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            throw e;
        }
    }

    public Benefits createBenefits(Employee employee) {
        Benefits benefits = new Benefits();
        benefits.setEmployee(employee);
        benefits.setHealthInsurance(50000.0);
        benefits.setProvidentFund(employee.getBasicSalary() * 0.12);
        benefits.setGratuity(employee.getBasicSalary() * 0.0481);
        benefits.setMealCard(true);
        benefits.setTransportAllowance(true);
        benefits.setEffectiveDate(LocalDate.now().toString());
        benefits.setActive(true);

        try {
            session.beginTransaction();
            session.save(benefits);
            session.getTransaction().commit();
            return benefits;
        } catch (Exception e) {
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            throw e;
        }
    }

    public Benefits updateBenefits(Benefits benefits) {
        try {
            session.beginTransaction();
            session.update(benefits);
            session.getTransaction().commit();
            return benefits;
        } catch (Exception e) {
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            throw e;
        }
    }
}