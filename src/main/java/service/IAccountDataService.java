package service;
import model.Account;

import java.util.Collection;

public interface IAccountDataService {

    Account get(long id);
    Collection<Account> getAll();
    Account create ();
    void credit (double amount, Account account);
    void debit (double amount, Account account);
    void delete (long id);
}