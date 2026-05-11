package infrastructure;

import dao.MatchDao;
import dao.PlayerDao;
import dao.impl.HibernateMatchDao;
import dao.impl.HibernatePlayerDao;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.SessionFactory;

public class DaoContainer {
    private final PlayerDao playerDao;
    private final MatchDao matchDao;

    public DaoContainer(SessionFactory factory) {
        this.playerDao = new HibernatePlayerDao(factory);
        this.matchDao = new HibernateMatchDao(factory);
    }

    public PlayerDao playerDao() {
        return playerDao;
    }
    public MatchDao matchDao() {
        return matchDao;
    }
}
