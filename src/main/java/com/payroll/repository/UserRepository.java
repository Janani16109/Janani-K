package com.payroll.repository;

import com.payroll.entity.User;
import org.hibernate.Session;

public class UserRepository {
    private final Session session;

    public UserRepository(Session session) {
        this.session = session;
    }

    public User save(User user) {
        session.beginTransaction();
        session.save(user);
        session.getTransaction().commit();
        return user;
    }

    public User findById(Long id) {
        return session.get(User.class, id);
    }

    public User findByUsername(String username) {
        return session.createQuery("from User u where u.username = :username", User.class)
                .setParameter("username", username)
                .uniqueResult();
    }
}