package service;

import java.util.Collection;
import model.Account;

public interface IAccountDataService {

    Account create ();
    void delete (long id);
    void update (Account account);
    Account get(long id);
    Collection<Account> getAll();
}