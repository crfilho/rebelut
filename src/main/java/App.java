import controller.TransactionController;
import data.VolatileTransactionRepository;
import org.slf4j.Logger;
import service.AccountDataService;
import controller.AccountController;
import io.javalin.Javalin;
import io.javalin.plugin.json.JavalinJson;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.LoggerFactory;
import service.IAccountDataService;
import service.ITransactionDataService;
import service.TransactionDataService;

import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.post;
import static io.javalin.apibuilder.ApiBuilder.put;
import static io.javalin.apibuilder.ApiBuilder.delete;
import static io.javalin.apibuilder.ApiBuilder.after;

public class App {

    private static String endpoint = "/api/v1/";
    private static int port = 8181;
    private static final Logger LOG = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {

        Gson gson = new GsonBuilder().create();
        JavalinJson.setFromJsonMapper(gson::fromJson);
        JavalinJson.setToJsonMapper(gson::toJson);
        Javalin app = Javalin.create().start(port);

        IAccountDataService accountDataService = new AccountDataService();
        AccountController accountController = new AccountController(accountDataService);

        ITransactionDataService transactionService = new TransactionDataService(new VolatileTransactionRepository(), accountDataService);
        TransactionController transactionController = new TransactionController(transactionService);

        app.routes(() -> {
            path(endpoint, () -> {
                path("accounts", () -> {
                    get(accountController.getAll);
                    post(accountController.create);
                    path(":id", () -> {
                        get(accountController.get);
                        delete(accountController.delete);
                        put(accountController.delete);
                    });
                    path(":accountid", () -> {
                        post("/deposit/:sum", transactionController.deposit);
                        post("/withdraw/:sum", transactionController.withdraw);
                        get("/statements", transactionController.getTransactions);
                    });
                });
                path("transactions", () -> {
                    post("send", transactionController.transfer);
                });
            });
            after(ctx -> ctx.contentType("application/json"));
        });

        app.exception(Exception.class, (e, ctx) -> {
            LOG.error(e.toString());
            ctx.status(500).json(e.getMessage());
        });
    }
}
