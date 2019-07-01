package service;
import model.Account;
import model.Transaction;
import service.TransactionDataService;
import data.ITransactionRepository;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class TransactionServiceTest {

    private static ITransactionRepository repoMock;
    private static IAccountDataService accService;
    private static TransactionDataService service;

    @Before
    public void prep()
    {
        accService = mock(IAccountDataService.class);
        repoMock = mock(ITransactionRepository.class);
        service = new TransactionDataService(repoMock, accService);
    }

    @Test
    public void depositTest() {

        Account a = new Account();
        when(accService.create()).thenReturn(a);
        when(accService.get(anyLong())).thenReturn(a);

        Account acc = accService.create();
        Transaction t = service.deposit(100.01d, acc.getId());
        assertEquals(acc.getBalance(), BigDecimal.valueOf(100.01));
    }

    @Test(expected = RuntimeException.class)
    public void rejectDepositInvalidAccountTest() {

        service.deposit(100.01d, 1020L);
    }

    @Test
    public void withdrawTest() {

        Account a = new Account();
        when(accService.create()).thenReturn(a);
        when(accService.get(anyLong())).thenReturn(a);

        Account acc = accService.create();
        service.deposit(100.00d, acc.getId());
        service.withdraw(20.00d, acc.getId());
        assertEquals(acc.getBalance(), BigDecimal.valueOf(80.00));
    }

    @Test(expected = RuntimeException.class)
    public void rejectWithdrawInvalidAccountTest() {

        service.withdraw(50.01d, 1030L);
    }

    @Test(expected = RuntimeException.class)
    public void rejectWithdrawInsufficientFunds() {

        Account a = accService.create();
        service.withdraw(50.02d, a.getId());
    }

    @After
    public void tearDown() {
        accService = null;
        repoMock = null;
        service = null;
     }
}