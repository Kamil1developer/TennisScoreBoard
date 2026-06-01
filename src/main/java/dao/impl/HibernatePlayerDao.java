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
        return sessionFactory.getCurrentSession().get(Player.class, id);
    }
}
