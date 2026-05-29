package dao.impl;

import dao.PlayerDao;
import entity.Player;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;
@AllArgsConstructor // Даже если сейчас у класса есть только final поля, то всё равно лучше использовать
    // максимально "узкую" аннотацию `@RequiredArgsConstructor` вместо `@AllArgsConstructor`.
    // Чтобы при добавлении новых non-final полей они автоматически не попадали в параметры конструктора.
public class HibernatePlayerDao implements PlayerDao {

    // TODO: Класс использует `sessionFactory.openSession()` для получения сессии. Это ведёт к антипаттерну "Session-per-Operation" ("сессия на операцию")
        // (см. файл "dao.md" в этом же пакете)

    // TODO: В блоках `catch` вызов `transaction.rollback()` не обёрнут в `try-catch`.
        // (см. файл "dao.md" в этом же пакете)

    private final SessionFactory sessionFactory;

    public Player insert(Player player) {
        Transaction transaction = null;
        try(Session session = sessionFactory.openSession() ) {
            transaction = session.beginTransaction();
            session.persist(player);

            transaction.commit();

            return player;
        }

        // TODO: Ловится слишком общее исключение. (см. файл "dao.md" в этом же пакете)
        catch (Exception e){

            // Перед откатом транзакции надо проверить, что она активна (isActive())
            if (transaction != null) {
                transaction.rollback();
            }

            // TODO: Лучше создать специальное исключение и выбрасывать его. (см. файл "dao.md" в этом же пакете)
            throw e;
        }
    }

    // Этот метод можно выполнять без транзакции
    public Player findByID(Long id){
        Transaction transaction = null;
        try(Session session = sessionFactory.openSession() ) {
            transaction = session.beginTransaction();
            Player player = session.get(Player.class, id);

            transaction.commit();

            return player;
        }

        // TODO: Ловится слишком общее исключение. (см. файл "dao.md" в этом же пакете)
        catch (Exception e){

            // Перед откатом транзакции надо проверить, что она активна (isActive())
            if (transaction != null) {
                transaction.rollback();
            }

            // TODO: Лучше создать специальное исключение и выбрасывать его. (см. файл "dao.md" в этом же пакете)
            throw e;
        }
    }
}
