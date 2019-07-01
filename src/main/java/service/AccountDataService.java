package service;
import model.Account;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccountDataService implements IAccountDataService {

    private final Logger LOG = LoggerFactory.getLogger(AccountDataService.class);
    private final ConcurrentHashMap<Long, Account> CACHE = new ConcurrentHashMap<>();

    @Override
    public Account get(long id) {
        LOG.info("account.get {}", id);
        return CACHE.get(id);
    }

    @Override
    public Collection<Account> getAll() {

        LOG.info("account.getAll");
        return CACHE.values();
    }

    @Override
    public Account create() {

        Account account = new Account();
        LOG.info("account.create {}", account);
        CACHE.put(account.getId(), account);
        return account;
    }

    @Override
    public void update(Account account) {

        LOG.info("account.update {}", account);
        CACHE.put(account.getId(), account);
    }

    @Override
    public void delete(long id) {

        LOG.info("account.delete {}", id);
        CACHE.remove(id);
    }
}