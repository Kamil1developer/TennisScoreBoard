package transaction;

import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.function.Supplier;

@RequiredArgsConstructor
public class TransactionManager {
    private final SessionFactory sessionFactory;

    public <T> void executeInTransaction(Supplier<T> action){
        Transaction transaction = null;
        try(Session session = sessionFactory.openSession() ) {
            transaction = session.beginTransaction();
            action.get();

            transaction.commit();

        }
        catch (Exception e){
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }
}
