package dao.impl;

import dao.PlayerDao;
import entity.Player;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;
@AllArgsConstructor
public class HibernatePlayerDao implements PlayerDao {
    private final SessionFactory sessionFactory;

    public Player save(Player player) {
        sessionFactory.getCurrentSession().persist(player);

        return player;
    }

    public Player findByID(Long id){
        Transaction transaction = null;
        try(Session session = sessionFactory.openSession() ) {
            transaction = session.beginTransaction();
            Player player = session.get(Player.class, id);

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
