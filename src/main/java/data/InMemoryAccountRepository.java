package data;
import model.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.IAccountDataService;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryAccountRepository implements IAccountRepository {

    private final ConcurrentHashMap<Long, Account> CACHE = new ConcurrentHashMap<>();

    @Override
    public Account get(long id) {
        return CACHE.get(id);
    }

    @Override
    public Collection<Account> getAll() {

        return CACHE.values();
    }

    @Override
    public void create(Account account) {

        CACHE.put(account.getId(), account);
    }

    @Override
    public void update(Account account) {

        CACHE.put(account.getId(), account);
    }

    @Override
    public void delete(long id) {

        CACHE.remove(id);
    }
}