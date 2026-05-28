package bootstrap;

import entity.Match;
import entity.Player;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class TestDataInitializer {

    // Более читаемым способом наполнить БД тестовыми данными при старте было бы создание файла
        // `src/main/resources/import.sql` и настройка его автоматического выполнения:
        // `<property name="hibernate.hbm2ddl.auto">create-drop</property>` в `hibernate.cfg.xml`.
    // Добавил для примера файл `src/main/resources/import.sql` с комментариями.

    private final SessionFactory factory;
    public TestDataInitializer(SessionFactory factory){
        this.factory = factory;
    }

    // Этот метод не выполняет никакой важной работы — просто оборачивает выполнение createTestMatches
    public void initialize(){
        createTestMatches();
    }
    private void createTestMatches(){
        Transaction transaction = null;
        try (Session session = factory.openSession()) {
            Player firstPlayer;
            Player secondPlayer;
            Player winnerPlayer;

            transaction = session.beginTransaction();

            List<List<String>> stringList = List.of(
                    List.of("Rafael Nadal", "Roger Federer"),
                    List.of("Mark", "Jack"),
                    List.of("Nikol", "Tom"),
                    List.of("Mark", "Jack")
            );

            // Чтобы добавить больше тестовых данных (например, для тестирования пагинации) или создать тестовый матч
                // с какими-то определёнными данными, придётся менять сложную логику в этом цикле.
            for (int i = 0; i < 4; i++) {
                firstPlayer = new Player(stringList.get(i).getFirst());
                secondPlayer = new Player(stringList.get(i).get(1)); // Можно использовать List.getLast(): stringList.get(i).getLast()
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
                if(i != 3) {

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
