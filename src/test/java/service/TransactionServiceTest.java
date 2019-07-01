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

    @Test
    public void transferTest() {

        Account from = new Account();
        Account to = new Account();

        when(accService.get(from.getId())).thenReturn(from);
        when(accService.get(to.getId())).thenReturn(to);

        service.deposit(200.00d, from.getId());
        service.transfer(50.00d, from.getId(), to.getId());
        assertEquals(BigDecimal.valueOf(150.00), from.getBalance());
        assertEquals(BigDecimal.valueOf(50.00), to.getBalance());
    }

    @Test(expected = RuntimeException.class)
    public void rejectTransferInvalidDestAccountTest() {

        Account orig = new Account();
        when(accService.get(orig.getId())).thenReturn(orig);

        service.transfer(250.01d, orig.getId(), 1040L);
    }

    @Test(expected = RuntimeException.class)
    public void rejectTransferInvalidOrigAccountTest() {

        Account dest = new Account();
        when(accService.get(dest.getId())).thenReturn(dest);

        service.transfer(250.01d, 1030L, dest.getId());
    }

    @Test(expected = RuntimeException.class)
    public void rejectTransferInvalidAmountTest() {

        Account dest = new Account();
        Account orig = new Account();
        when(accService.get(dest.getId())).thenReturn(dest);
        when(accService.get(orig.getId())).thenReturn(orig);

        service.transfer(250.00d, orig.getId(), dest.getId());
    }

    @Test(expected = RuntimeException.class)
    public void rejectTransferInsufficientFunds() {

        Account from = accService.create();
        Account to = accService.create();
        service.transfer(50.02d, from.getId(), to.getId());
    }

    @After
    public void tearDown() {
        accService = null;
        repoMock = null;
        service = null;
     }
}