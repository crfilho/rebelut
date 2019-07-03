import controller.AccountController;
import controller.TransactionController;
import data.IAccountRepository;
import data.InMemoryAccountRepository;
import service.AccountDataService;
import service.IAccountDataService;
import service.ITransactionDataService;
import service.TransactionDataService;
import data.InMemoryTransactionRepository;
import validator.BasicTransactionValidator;

public class App {

    public static void main(String[] args) {

        IAccountDataService accountDataService = new AccountDataService(new InMemoryAccountRepository());
        AccountController accountController = new AccountController(accountDataService);

        ITransactionDataService transactionService = new TransactionDataService(new BasicTransactionValidator(), new InMemoryTransactionRepository(), accountDataService);
        TransactionController transactionController = new TransactionController(transactionService);

        new RestApi(accountController, transactionController).start();
    }
}
