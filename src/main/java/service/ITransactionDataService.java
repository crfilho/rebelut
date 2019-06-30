package service;
import model.Account;
import model.Transaction;
import java.util.Collection;

public interface ITransactionDataService {
    Collection<Transaction> getAll (Account account);
    Transaction transfer (double sum, Account orig, Account dest);
    Transaction deposit (double sum, Account accountid);
    Transaction withdraw (double sum, Account accountid);
}