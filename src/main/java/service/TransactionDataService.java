package service;
import model.Account;
import model.Block;
import model.Transaction;
import model.TransactionType;
import data.ITransactionRepository;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransactionDataService implements ITransactionDataService {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionDataService.class);
    private ITransactionRepository transactionRepository;
    private IAccountDataService accountService;

    public TransactionDataService(ITransactionRepository transactionRepository, IAccountDataService accountService) {
        this.transactionRepository = transactionRepository;
        this.accountService = accountService;
    }

    @Override
    public Collection<Transaction> getAll (long accountid) throws RuntimeException {

        LOG.info("getTransactions for account {}", accountid);
        Account account = accountService.get(accountid);

        if (account == null) throw new RuntimeException("Invalid account");
        return transactionRepository.getAll(accountid);
    }

    @Override
    public synchronized Transaction deposit (double amount, long accountid) throws RuntimeException {

        LOG.info("deposit {} into account {}", amount, accountid);
        Account account = accountService.get(accountid);

        if (account == null) throw new RuntimeException("Invalid account");
        Transaction t = new Transaction(TransactionType.DEPOSIT, amount, accountid);
        transactionRepository.store(t);

        account.sum(amount);
        accountService.update(account);
        t.setStatus(1);
        return t;
    }

    @Override
    public synchronized Transaction withdraw (double amount, long accountid) throws RuntimeException {

        LOG.info("withdraw {} from account {}", amount, accountid);
        Account account = accountService.get(accountid);

        if (account == null) throw new RuntimeException("Invalid account");
        if (account.getBalance().compareTo(BigDecimal.valueOf(amount)) < 0) throw new RuntimeException("Insufficient funds");

        Transaction t = new Transaction(TransactionType.WITHDRAW, amount*-1, account.getId());
        transactionRepository.store(t);

        account.subtract(amount);
        accountService.update(account);

        t.setStatus(1);
        return t;
    }

    @Override
    public synchronized Transaction[] transfer (double amount, long from, long to) throws RuntimeException {

        LOG.info("transfer {} from {} to {}", amount, from, to);

        Account orig = accountService.get(from);
        Account dest = accountService.get(to);

        if (orig == null || dest == null || orig == dest) throw new RuntimeException("Invalid transaction");
        if (orig.getBalance().compareTo(BigDecimal.valueOf(amount)) < 0) throw new RuntimeException("Insufficient funds");

        Transaction in = new Transaction(TransactionType.TRANSFER_IN, amount * -1, from);
        Transaction out = new Transaction(TransactionType.TRANSFER_OUT, amount, to);
        transactionRepository.store(in);
        transactionRepository.store(out);

        synchronized(orig) {

            orig.subtract(amount);
            accountService.update(orig);
            out.setStatus(1);
        }
        synchronized(dest) {

            dest.sum(amount);
            accountService.update(dest);
            in.setStatus(1);
        }
        return new Transaction[] {in, out};
    }

    private Block process(Transaction tx) {
        /*// TODO validate balance ?
        Block top = chain.get(accountId);
        Block incoming = new Block(top, sum);

        chain.put(accountId, incoming);

        return chain.get(accountId);*/
        return null;
    }
}
