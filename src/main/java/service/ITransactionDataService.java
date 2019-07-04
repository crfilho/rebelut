package service;
import model.Account;
import model.Transaction;
import model.Transfer;
import java.util.Collection;

public interface ITransactionDataService {
    Object getAll (long account);
    Transfer transfer (double amount, long from, long to);
    Transaction deposit (double amount, long accountid);
    Transaction withdraw (double amount, long accountid);
}