package controller;
import io.javalin.http.Handler;
import model.Account;
import service.IAccountDataService;

public class AccountController {

    private IAccountDataService accountService;

    public AccountController(IAccountDataService accountService) {
        this.accountService = accountService;
    }

    public Handler get = ctx -> {
        try {
            long id = Long.valueOf(ctx.pathParam("id"));
            ctx.json(accountService.get(id));
        }
        catch(Exception e) {
            ctx.status(400).json(e.getMessage());
        }
    };

    public Handler getAll = ctx -> {
        try {
            ctx.json(accountService.getAll());
        }
        catch(Exception e) {
            ctx.status(400).json(e.getMessage());
        }
    };

    public Handler create = ctx -> {
        try {
            Account acc = accountService.create();
            ctx.status(201).json(acc);
        }
            catch(Exception e) {
            ctx.status(400).json(e.getMessage());
        }
    };

    public Handler delete = ctx -> {
        try {
            long id = Long.valueOf(ctx.pathParam("id"));
            accountService.delete(id);
            ctx.status(200);
        }
        catch(Exception e) {
            ctx.status(400).json(e.getMessage());
        }
    };
}