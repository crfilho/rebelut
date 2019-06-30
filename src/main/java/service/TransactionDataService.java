package service;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import data.ITransactionRepository;
import model.TransactionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import model.Account;
import model.Block;
import model.Transaction;

public class TransactionDataService implements ITransactionDataService {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionDataService.class);
    private ITransactionRepository transactionRepository;

    public TransactionDataService(ITransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Collection<Transaction> getAll (Account account) {

        LOG.info("getTransactions for account {}", account);

        return transactionRepository.getAll(account.getId());
    }

    @Override
    public synchronized Transaction deposit (double sum, Account account) {

        LOG.info("deposit {} into account {}", sum, account);
        Transaction t = new Transaction(TransactionType.DEPOSIT, sum, account.getId());
        transactionRepository.store(t);

        account.setBalance(account.getBalance().add(BigDecimal.valueOf(sum)));
        return t;
    }

    @Override
    public synchronized Transaction withdraw (double sum, Account account) {

        //checkAmount(amount);
        LOG.info("withdraw {} from account {}", sum, account);
        Transaction t = new Transaction(TransactionType.WITHDRAW, sum*-1, account.getId());
        transactionRepository.store(t);

        account.setBalance(account.getBalance().subtract(BigDecimal.valueOf(sum)));
        return t;
    }

    @Override
    public synchronized Transaction transfer (double sum, Account orig, Account dest) {

        //checkAmount(amount);
        LOG.info("transfer {} from {} to {}", sum, orig, dest);

        Transaction in = new Transaction(TransactionType.TRANSFER_OUT, sum, orig.getId());
        Transaction out = new Transaction(TransactionType.TRANSFER_IN, sum * -1, dest.getId());
        transactionRepository.store(in);
        transactionRepository.store(out);

        orig.setBalance(orig.getBalance().add(BigDecimal.valueOf(sum)));
        dest.setBalance(dest.getBalance().subtract(BigDecimal.valueOf(sum)));
        return out;
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
