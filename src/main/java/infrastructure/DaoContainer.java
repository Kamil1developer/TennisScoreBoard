package infrastructure;

import dao.PlayerDao;
import dao.impl.HibernatePlayerDao;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.SessionFactory;

public class DaoContainer {
    private final PlayerDao playerDao;

    public DaoContainer(SessionFactory factory) {
        this.playerDao = new HibernatePlayerDao(factory);
    }

    public PlayerDao playerDao() {
        return playerDao;
    }
}
