package service;
import data.ITransactionRepository;
import model.Account;
import model.Transaction;
import model.TransactionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import validator.ITransactionValidator;
import validator.InvalidTransactionException;
import validator.TransactionErrorMessage;

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
    public Collection<Transaction> getAll (long accountid) throws RuntimeException {

        LOG.info("getTransactions for account {}", accountid);
        Account account = accountService.get(accountid);

        if (account == null)
            throw new InvalidTransactionException(TransactionErrorMessage.INVALID_ACCOUNT);

        return transactionRepository.getAll(accountid);
    }

    @Override
    public Transaction deposit (double amount, long accountid) throws InvalidTransactionException {

        LOG.info("deposit {} into account {}", amount, accountid);
        Account account = accountService.get(accountid);

        Transaction transaction = new Transaction(TransactionType.DEPOSIT, amount, account);
        transactionValidator.validate(transaction);

        accountService.credit(amount, account);

        store(transaction);
        return transaction;
    }

    @Override
    public Transaction withdraw (double amount, long accountid) throws InvalidTransactionException {

        LOG.info("withdraw {} from account {}", amount, accountid);
        Account account = accountService.get(accountid);

        Transaction transaction = new Transaction(TransactionType.WITHDRAW, amount * -1, account);
        transactionValidator.validate(transaction);

        accountService.debit(amount, account);

        store(transaction);
        return transaction;
    }

    @Override
    public Transaction[] transfer (double amount, long from, long to) throws InvalidTransactionException {

        LOG.info("transfer {} from {} to {}", amount, from, to);

        Account orig = accountService.get(from);
        Account dest = accountService.get(to);

        Transaction cashOut = new Transaction(TransactionType.TRANSFER_OUT, amount * -1, orig);
        Transaction cashIn = new Transaction(TransactionType.TRANSFER_IN, amount, dest);

        transactionValidator.validateTransfer(cashOut, cashIn);

        accountService.debit(amount, orig);
        accountService.credit(amount, dest);

        store(cashOut, cashIn);
        return new Transaction[] { cashIn, cashOut };
    }

    private void store (Transaction ... transactions) {

        for (Transaction t: transactions) {
            t.setStatus(1);
            transactionRepository.store(t);
        }
    }
}
