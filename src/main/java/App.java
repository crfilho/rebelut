import controller.AccountController;
import controller.TransactionController;
import service.AccountDataService;
import service.IAccountDataService;
import service.ITransactionDataService;
import service.TransactionDataService;
import data.VolatileTransactionRepository;

public class App {

    public static void main(String[] args) {

        IAccountDataService accountDataService = new AccountDataService();
        AccountController accountController = new AccountController(accountDataService);

        ITransactionDataService transactionService = new TransactionDataService(new VolatileTransactionRepository(), accountDataService);
        TransactionController transactionController = new TransactionController(transactionService);

        new RestApi(accountController, transactionController).start();
    }
}
