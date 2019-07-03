package data;
import model.Transaction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryTransactionRepository implements ITransactionRepository {

     private static final ConcurrentHashMap<Long, Collection<Transaction>> TX_CACHE = new ConcurrentHashMap<>();

    @Override
    public void store (Transaction transaction) {

        Collection<Transaction> transactions = TX_CACHE.containsKey(transaction.getAccount().getId())
                ? TX_CACHE.get(transaction.getAccount().getId())
                : new ArrayList<>();

        transactions.add(transaction);
        TX_CACHE.put(transaction.getAccount().getId(), transactions);
    }

    @Override
    public Collection<Transaction> getAll(long accountid) {

        if (TX_CACHE.containsKey(accountid))
            return TX_CACHE.get(accountid);
        else
            return new ArrayList<Transaction>();
    }
}
