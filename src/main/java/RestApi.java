import controller.AccountController;
import controller.TransactionController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.javalin.Javalin;
import io.javalin.plugin.json.JavalinJson;

import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.post;
import static io.javalin.apibuilder.ApiBuilder.delete;
import static io.javalin.apibuilder.ApiBuilder.after;

public class RestApi {

    private static String endpoint = "/api/v1/";
    private static int port = 8181;
    private static final Logger LOG = LoggerFactory.getLogger(App.class);

    private Javalin app;
    private AccountController accountController;
    private TransactionController transactionController;

    public RestApi(AccountController accountController, TransactionController transactionController) {
        this.accountController = accountController;
        this.transactionController = transactionController;
    }

    public void start()
    {
        setupService();
        setupRouting();
    }

    private void setupService()
    {
        Gson gson = new GsonBuilder().create();
        JavalinJson.setFromJsonMapper(gson::fromJson);
        JavalinJson.setToJsonMapper(gson::toJson);
        app = Javalin.create().start(port);
    }

    private void setupRouting()
    {
        app.routes(() -> {
            path(endpoint, () -> {
                path("accounts", () -> {
                    get(accountController.getAll);
                    post(accountController.create);
                    path(":id", () -> {
                        get(accountController.get);
                        delete(accountController.delete);
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
