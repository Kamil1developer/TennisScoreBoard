package dao.impl;

import dao.PlayerDao;
import entity.Player;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class HibernatePlayerDao implements PlayerDao {
    private final SessionFactory sessionFactory;

    public HibernatePlayerDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Player> findAll() {
        return List.of();
    }


    public Player insert(Player player) {
        Transaction transaction = null;
        try(Session session = sessionFactory.openSession() ) {
            transaction = session.beginTransaction();
            session.persist(player);

            transaction.commit();

            return player;
        }
        catch (Exception e){
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }
}
