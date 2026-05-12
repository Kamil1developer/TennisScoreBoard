package dao.impl;

import dao.MatchDao;
import entity.Match;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

@AllArgsConstructor
public class HibernateMatchDao implements MatchDao {
    private final SessionFactory sessionFactory;

    public void insert(Match match) {
        Transaction transaction = null;
        try(Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(match);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    public void findById() {

    }

    public void findAll() {

    }
}
