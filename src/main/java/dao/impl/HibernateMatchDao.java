package dao.impl;

import dao.MatchDao;
import entity.Match;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

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

    public Optional<List<Match>> findAll() {
        Transaction transaction = null;

        try(Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            List<Match> matches = session.createQuery("from Match", Match.class).getResultList();

            transaction.commit();
            return Optional.of(matches);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
        return Optional.empty();
    }
}
