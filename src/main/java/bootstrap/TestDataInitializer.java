package bootstrap;

import entity.Match;
import entity.Player;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

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

            List<List<String>> stringList = List.of(
                    List.of("Rafael Nadal", "Roger Federer"),
                    List.of("Johnson", "Mike"),
                    List.of("Nikol", "Tom"),
                    List.of("Mark", "Jack")
            );

            for (int i = 0; i < 4; i++) {
                firstPlayer = new Player(stringList.get(i).getFirst());
                secondPlayer = new Player(stringList.get(i).get(1));
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
                if(i != 3) {
                    winnerPlayer = secondPlayer;
                    match = new Match(firstPlayer, secondPlayer, winnerPlayer);
                    session.persist(match);

                    winnerPlayer = firstPlayer;
                    match = new Match(firstPlayer, secondPlayer, winnerPlayer);
                    session.persist(match);
                }
            }


            transaction.commit();
        }
        catch (Exception e){
            if (transaction != null){
                transaction.rollback();
            }
        }
    }
}
