package service;
import data.IAccountRepository;
import model.Account;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccountDataService implements IAccountDataService {

    private IAccountRepository accountRepository;
    private final Logger LOG = LoggerFactory.getLogger(AccountDataService.class);

    private AccountDataService() { }
    public AccountDataService(IAccountRepository accountRepository) {
        this.accountRepository= accountRepository;
    }

    @Override
    public Account get(long id) {

        LOG.info("account.get {}", id);
        return accountRepository.get(id);
    }

    @Override
    public Collection<Account> getAll() {

        LOG.info("account.getAll");
        return accountRepository.getAll();
    }

    @Override
    public Account create() {

        Account account = new Account();
        LOG.info("account.create {}", account);

        accountRepository.create(account);
        return account;
    }

    @Override
    public void credit(double amount, Account account) {

        LOG.info("account.update {}", account);

        account.sum(amount);
        accountRepository.update(account);
    }

    @Override
    public void debit(double amount, Account account) {

        LOG.info("account.update {}", account);

        account.subtract(amount);
        accountRepository.update(account);
    }

    @Override
    public void delete(long id) {

        LOG.info("account.delete {}", id);
        accountRepository.delete(id);
    }
}