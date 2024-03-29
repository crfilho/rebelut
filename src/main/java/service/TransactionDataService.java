package service;
import data.ITransactionRepository;
import model.Account;
import model.Transaction;
import model.TransactionType;
import model.Transfer;
import validator.ITransactionValidator;
import validator.exceptions.InvalidAccountException;
import validator.exceptions.InvalidTransactionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

public class TransactionDataService implements ITransactionDataService {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionDataService.class);
    private ITransactionValidator transactionValidator;
    private ITransactionRepository transactionRepository;
    private IAccountDataService accountService;

    private TransactionDataService() { }

    public TransactionDataService(ITransactionValidator transactionValidator,
                                  ITransactionRepository transactionRepository,
                                  IAccountDataService accountService) {

        this.transactionValidator = transactionValidator;
        this.transactionRepository = transactionRepository;
        this.accountService = accountService;
    }

    @Override
    public Collection<Transaction> getAll (long accountid) {

        LOG.info("getTransactions for account {}", accountid);

        return transactionRepository.getAll(accountid);
    }

    @Override
    public synchronized Transaction deposit (double amount, long accountid) throws InvalidTransactionException, InvalidAccountException {

        LOG.info("deposit {} into account {}", amount, accountid);

        Account account = accountService.get(accountid);
        Transaction transaction = new Transaction(TransactionType.DEPOSIT, amount, account);
        transactionValidator.validateTransaction(transaction);

        accountService.credit(amount, account);

        store(transaction);
        return transaction;
    }

    @Override
    public synchronized Transaction withdraw (double amount, long accountid) throws InvalidTransactionException, InvalidAccountException {

        LOG.info("withdraw {} from account {}", amount, accountid);

        Account account = accountService.get(accountid);
        Transaction transaction = new Transaction(TransactionType.WITHDRAW, amount * -1, account);
        transactionValidator.validateTransaction(transaction);

        accountService.debit(amount, account);

        store(transaction);
        return transaction;
    }

    @Override
    public synchronized Transfer transfer (double amount, long from, long to) throws InvalidTransactionException, InvalidAccountException {

        LOG.info("transfer {} from {} to {}", amount, from, to);

        Account orig = accountService.get(from);
        Account dest = accountService.get(to);

        Transfer transfer = new Transfer(amount, orig, dest);
        transactionValidator.validateTransfer(transfer);

        accountService.debit(amount, orig);
        accountService.credit(amount, dest);

        store(transfer.getOrigTransaction(), transfer.getDestTransaction());
        return transfer;
    }

    private void store (Transaction ... transactions) {

        for (Transaction t: transactions) {
            t.setStatus(1);
            transactionRepository.store(t);
        }
    }
}
