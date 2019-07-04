import controller.AccountController;
import controller.TransactionController;
import data.InMemoryRepository;
import service.AccountDataService;
import service.IAccountDataService;
import service.ITransactionDataService;
import service.TransactionDataService;
import validator.TransactionValidator;

public class App {

    public static void main(String[] args) {

        IAccountDataService accountDataService = new AccountDataService(InMemoryRepository.instance());
        AccountController accountController = new AccountController(accountDataService);

        ITransactionDataService transactionService = new TransactionDataService(new TransactionValidator(), InMemoryRepository.instance(), accountDataService);
        TransactionController transactionController = new TransactionController(transactionService);

        new RestAPI(accountController, transactionController).start();
    }
}
