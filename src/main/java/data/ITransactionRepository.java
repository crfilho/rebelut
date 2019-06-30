package data;
import model.Transaction;
import java.util.Collection;

public interface ITransactionRepository {
    void store (Transaction transaction);
    Collection<Transaction> getAll(long accountid);
}
