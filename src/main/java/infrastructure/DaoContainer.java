package infrastructure;

import dao.impl.PlayerDao;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.SessionFactory;

public class DaoContainer {
    private final PlayerDao playerDao;

    public DaoContainer(SessionFactory factory) {
        this.playerDao = new PlayerDao(factory);
    }

    public PlayerDao playerDao() {
        return playerDao;
    }
}
