package bootstrap;

import entity.Match;
import entity.Player;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class TestDataInitializer {
    private final SessionFactory factory;
    public TestDataInitializer(SessionFactory factory){
        this.factory = factory;
    }
    public void initialize(){
        createTestMatches();
    }
    public void createTestMatches(){
        Transaction transaction = null;
        try (Session session = factory.openSession()) {
            Player firstPlayer;
            Player secondPlayer;
            Player winnerPlayer;

            transaction = session.beginTransaction();
            firstPlayer = new Player("Rafael Nadal");
            secondPlayer = new Player("Roger Federer");
            session.persist(firstPlayer);
            session.persist(secondPlayer);

            winnerPlayer = firstPlayer;
            Match match = new Match(firstPlayer, secondPlayer, winnerPlayer);
            session.persist(match);

            winnerPlayer = secondPlayer;
            match = new Match(firstPlayer, secondPlayer, winnerPlayer);
            session.persist(match);

            winnerPlayer = firstPlayer;
            match = new Match(firstPlayer, secondPlayer, winnerPlayer);
            session.persist(match);

            winnerPlayer = secondPlayer;
            match = new Match(firstPlayer, secondPlayer, winnerPlayer);
            session.persist(match);

            winnerPlayer = firstPlayer;
            match = new Match(firstPlayer, secondPlayer, winnerPlayer);
            session.persist(match);

            transaction.commit();
        }
        catch (Exception e){
            if (transaction != null){
                transaction.rollback();
            }
        }
    }
}
