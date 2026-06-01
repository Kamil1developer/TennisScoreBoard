package transaction;

import java.util.function.Supplier;

public interface TransactionManager {
    <T> T executeInTransaction(Supplier<T> action);

    void executeInTransactionWithoutResult(Runnable action);
}
