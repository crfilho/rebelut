package service;
import model.Account;
import java.util.Collection;

public interface IAccountDataService {

    Account create ();
    void delete (long id);
    void update (Account account);
    Account get(long id);
    Collection<Account> getAll();
}