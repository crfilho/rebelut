package service;
import model.Transaction;
import model.Transfer;

public interface ITransactionDataService {
    Object getAll (long account);
    Transfer transfer (double amount, long from, long to);
    Transaction deposit (double amount, long accountid);
    Transaction withdraw (double amount, long accountid);
}