package controller;
import model.Account;
import model.Transaction;
import service.IAccountDataService;
import service.ITransactionDataService;
import java.util.Collection;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.javalin.http.Handler;

public class AccountController {

    private IAccountDataService accountService;

    public AccountController(IAccountDataService accountService) {
        this.accountService = accountService;
    }

    public Handler get = ctx -> {
        long id = Long.valueOf(ctx.pathParam("id"));
        Account a = accountService.get(id);
        if (a != null) ctx.json(a);
        else ctx.status(404);
    };

    public Handler getAll = ctx -> {
        ctx.json(accountService.getAll());
    };

    public Handler create = ctx -> {
        Account acc = accountService.create();
        ctx.status(201).json(acc);
    };

    public Handler delete = ctx -> {
        long id = Long.valueOf(ctx.pathParam("id"));
        accountService.delete(id);
        ctx.status(204);
    };
}