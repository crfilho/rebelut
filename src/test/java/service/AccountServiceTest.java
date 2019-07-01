package service;
import model.Account;
import org.junit.After;
import service.AccountDataService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class AccountServiceTest {

    private AccountDataService accountDataService;

    @Before
    public void prep()
    {
        accountDataService = new AccountDataService();
    }

    @Test
    public void createAndCacheAccount() {

        Account acc = accountDataService.create();
        assertEquals(acc, accountDataService.get(acc.getId()));
    }

    @Test
    public void createMultipleAccounts() {

        Account acc1 = accountDataService.create();
        Account acc2 = accountDataService.create();
        assertNotEquals(acc1, acc2);
    }

    @Test
    public void updateBalance() {

        Account acc = accountDataService.create();
        acc.sum(15.01);
        accountDataService.update(acc);
        assertEquals(BigDecimal.valueOf(15.01), accountDataService.get(acc.getId()).getBalance());
    }

    @Test
    public void getAllAccounts() {

        accountDataService.create();
        accountDataService.create();
        accountDataService.create();
        assertEquals(3, accountDataService.getAll().size());
    }

    @After
    public void tearDown()
    {
        accountDataService = null;
    }
}