package com.payroll.repository;

import com.payroll.entity.Tax;
import org.hibernate.Session;
import java.util.List;

public class TaxRepository {
    private final Session session;

    public TaxRepository(Session session) {
        this.session = session;
    }

    public Tax save(Tax tax) {
        session.beginTransaction();
        session.save(tax);
        session.getTransaction().commit();
        return tax;
    }

    public Tax findById(Long id) {
        return session.get(Tax.class, id);
    }

    public List<Tax> findByEmployeeId(Long employeeId) {
        return session.createQuery(
            "from Tax t where t.employee.id = :employeeId",
            Tax.class)
            .setParameter("employeeId", employeeId)
            .getResultList();
    }
}