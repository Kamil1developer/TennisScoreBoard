package dao.impl;

import dao.MatchDao;
import entity.Match;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor // Даже если сейчас у класса есть только final поля, то всё равно лучше использовать
    // максимально "узкую" аннотацию `@RequiredArgsConstructor` вместо `@AllArgsConstructor`.
    // Чтобы при добавлении новых non-final полей они автоматически не попадали в параметры конструктора.
public class HibernateMatchDao implements MatchDao {

    // TODO: Класс использует `sessionFactory.openSession()` для получения сессии. Это ведёт к антипаттерну "Session-per-Operation" ("сессия на операцию")
        // (см. файл "dao.md" в этом же пакете)

    // TODO: В блоках `catch` вызов `transaction.rollback()` не обёрнут в `try-catch`.
        // (см. файл "dao.md" в этом же пакете)

    // TODO: В методе findAll отсутствует явная сортировка результатов. Запрос HQL не содержит `ORDER BY`,
        // поэтому порядок возвращаемых записей зависит от реализации JPA (обычно по первичному ключу в порядке возрастания).
        // Это приводит к тому, что самые новые матчи отображаются в конце списка.
        // Пользователь, заходящий на страницу завершённых матчей, ожидает увидеть сначала последние завершённые матчи.
        // В текущей реализации ему приходится пролистывать пагинацию до конца, чтобы найти свежие результаты.
        // Это ухудшает пользовательский опыт и делает интерфейс неинтуитивным. При большом количестве матчей добираться до новых данных будет крайне неудобно.
        // Стоит добавить в HQL-запрос сортировку по убыванию идентификатора матча, так как это естественный способ упорядочить матчи от новых к старым.
        // Сейчас это исправляется разворотом matches.reversed() в сервисе, но так быть не должно.

    // TODO: Проблема N+1 запросов в методе выборки матчей.
        // Метод `findAll` выполняет HQL-запрос вида `"FROM Match m ..."`.
        // Сущность `Match` имеет связи `@ManyToOne` с `Player`, поэтому при выполнении такого запроса
        // Hibernate сначала получит список матчей (1 запрос), а затем он будет выполнять по 2 дополнительных `SELECT` запроса
        // для каждого матча, чтобы получить связанных с ним игроков. Если на странице 5 матчей,
        // это приведёт к 11 запросам вместо одного.

    private final SessionFactory sessionFactory;

    public void insert(Match match) {
        Transaction transaction = null;
        try(Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(match);

            transaction.commit();

        // TODO: Ловится слишком общее исключение. (см. файл "dao.md" в этом же пакете)
        } catch (Exception e) {

            // Перед откатом транзакции надо проверить, что она активна (isActive())
            if (transaction != null) {
                transaction.rollback();
            }

            // TODO: Исключение "проглатывается", что создаёт ложное впечатление, что матч сохранён успешно, даже когда это не так.
        }
    }

    public void findById() {

    }

    // Этот метод можно выполнять без транзакции
    public Optional<List<Match>> findAll() {
        Transaction transaction = null;

        try(Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            // Ключевые слова в тексте HQL-запросов (`from`) написаны в нижнем регистре.
                // Хотя это и не влияет на работоспособность, написание ключевых слов SQL/HQL в верхнем регистре (`UPPERCASE`) является общепринятым стандартом.
                // Это значительно улучшает читаемость запросов, так как визуально отделяет синтаксические конструкции языка от имён сущностей и полей.
            // Лучше вынести тексты этого и будущих HQL запросов в `private static final` константы и дать им понятные имена.
            List<Match> matches = session.createQuery("from Match", Match.class).getResultList();

            transaction.commit();
            return Optional.of(matches);

        // TODO: Ловится слишком общее исключение. (см. файл "dao.md" в этом же пакете)
        } catch (Exception e) {

            // Перед откатом транзакции надо проверить, что она активна (isActive())
            if (transaction != null) {
                transaction.rollback();
            }
        }

        // TODO: Исключение "проглатывается", что создаёт ложное впечатление, что выборка матчей пустая, даже когда это не так.
        return Optional.empty();
    }
}
