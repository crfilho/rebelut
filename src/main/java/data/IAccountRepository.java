package data;
import model.Account;

import java.util.Collection;

public interface IAccountRepository {

    Account get(long id);
    Collection<Account> getAll();
    void create(Account account);
    void update(Account account);
    void delete(long id);
}