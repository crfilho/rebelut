package service;
import model.Account;
import model.Transaction;
import java.util.Collection;

public interface ITransactionDataService {
    Collection<Transaction> getAll (long account);
    Transaction transfer (double amount, long from, long to);
    Transaction deposit (double amount, long accountid);
    Transaction withdraw (double amount, long accountid);
}