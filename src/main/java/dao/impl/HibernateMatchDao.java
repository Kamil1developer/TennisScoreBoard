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

    public void save(Match match) {
        sessionFactory.getCurrentSession().persist(match);
    }

    public List<Match> findAll() {
            List<Match> matches = sessionFactory.getCurrentSession().createQuery("from Match", Match.class).getResultList();

            return matches;
    }
}
