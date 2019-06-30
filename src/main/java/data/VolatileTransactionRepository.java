package data;
import model.Block;
import model.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class VolatileTransactionRepository implements ITransactionRepository {

    private static final ConcurrentHashMap<Long, Block> CHAIN = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Long, Collection<Transaction>> TX_CACHE = new ConcurrentHashMap<>();

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
        // TODO sort?
        if (TX_CACHE.containsKey(accountid))
            return TX_CACHE.get(accountid);
        else
            return new ArrayList<Transaction>();
    }
}
