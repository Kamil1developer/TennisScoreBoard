package transaction;

import exceptions.DataAccessException;
import lombok.RequiredArgsConstructor;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.function.Supplier;

@RequiredArgsConstructor
public class HibernateTransactionManager implements TransactionManager {
    private final SessionFactory sessionFactory;

    public <T> T executeInTransaction(Supplier<T> action){
        Transaction transaction = null;
        try {
            Session session = sessionFactory.getCurrentSession();
            transaction = session.beginTransaction();

            T result = action.get();

            transaction.commit();
            return result;

        }
        catch (HibernateException e){
            safeRollback(transaction, e);
            throw new DataAccessException();
        }
    }

    public void executeInTransactionWithoutResult(Runnable action) {
        executeInTransaction(() -> {
            action.run();
            return null;
        });
    }
    private void safeRollback(Transaction transaction, Exception originalException) {
        if (transaction != null && transaction.isActive()) {
            try {
                transaction.rollback();
            } catch (Exception rollbackException) {
                originalException.addSuppressed(rollbackException);
            }
        }
    }
}
