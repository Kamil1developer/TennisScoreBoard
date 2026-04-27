package dao.impl;

import dao.BaseDao;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.SessionFactory;

import java.util.List;

public class PlayerDao implements BaseDao {
    private final SessionFactory sessionFactory;

    public PlayerDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List findAll() {
        return List.of();
    }

    @Override
    public Object insert(Object entity) {
        return null;
    }
}
