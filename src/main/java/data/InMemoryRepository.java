package data;
import model.Account;
import model.Transaction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryRepository implements IAccountRepository, ITransactionRepository {

    private static InMemoryRepository instance = new InMemoryRepository();
    private static final Map<Long, Account> ACCOUNT_CACHE = new ConcurrentHashMap<>();
    private static final Map<Long, Collection<Transaction>> TX_CACHE = new ConcurrentHashMap<>();

    private InMemoryRepository() { }

    public static InMemoryRepository instance() {
        return instance;
    }

    @Override
    public Optional<Account> get(long id) {
        return Optional.ofNullable(ACCOUNT_CACHE.get(id));
    }

    @Override
    public Collection<Account> getAll() {

        return ACCOUNT_CACHE.values();
    }

    @Override
    public void create(Account account) {

        ACCOUNT_CACHE.put(account.getId(), account);
    }

    @Override
    public void update(Account account) {

        return;
    }

    @Override
    public void delete(long id) {

        ACCOUNT_CACHE.remove(id);
    }

    @Override
    public void store (Transaction transaction) {

        Collection<Transaction> transactions = TX_CACHE.containsKey(transaction.getAccountId())
                ? TX_CACHE.get(transaction.getAccountId())
                : new ArrayList<>();

        transactions.add(transaction);
        TX_CACHE.put(transaction.getAccountId(), transactions);
    }

    @Override
    public Collection<Transaction> getAll(long accountid) {

        if (TX_CACHE.containsKey(accountid))
            return TX_CACHE.get(accountid);
        else
            return new ArrayList<Transaction>();
    }
}