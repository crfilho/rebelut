package controller;
import io.javalin.http.Handler;
import model.Account;
import model.Transaction;
import service.IAccountDataService;
import service.ITransactionDataService;

import java.math.BigDecimal;

public class TransactionController {

    private ITransactionDataService transactionService;
    private IAccountDataService accountService;

    public TransactionController(ITransactionDataService transactionService, IAccountDataService accountService) {
        this.transactionService = transactionService;
        this.accountService = accountService;
    }

    public Handler deposit = ctx -> {

        long accountid = Long.valueOf(ctx.pathParam("accountid"));
        Account account = accountService.get(accountid);

        double sum = ctx.pathParam("sum", double.class).check(it -> it > 0).get();

        ctx.json(transactionService.deposit(sum, account));
    };

    public Handler withdraw = ctx -> {

        long accountid = Long.valueOf(ctx.pathParam("accountid"));
        Account account = accountService.get(accountid);

        //refactor;
        double sum = ctx.pathParam("sum", double.class).check(it -> it > 0).get();
        if (account == null) ctx.status(400);

        ctx.json(transactionService.withdraw(sum, account));
    };

    public Handler transfer = ctx -> {

        long from = Long.valueOf(ctx.formParam("from"));
        long to = Long.valueOf(ctx.formParam("to"));

        Account orig = accountService.get(from);
        Account dest = accountService.get(to);
        double sum = ctx.formParam("sum", double.class).check(it -> it > 0).get();

        if (orig == null || dest == null || orig == dest) ctx.status(400);
        if (orig.getBalance().subtract(BigDecimal.valueOf(sum)).signum() < 0) ctx.status(400).json("Insufficient balance");
        //if (t.getStatus() < 0) ctx.status (500);

        ctx.json(transactionService.transfer(sum, orig, dest));
    };

    public Handler getTransactions = ctx -> {

        long accountid = Long.valueOf(ctx.pathParam("accountid"));
        Account acc = accountService.get(accountid);

        if (acc==null) ctx.status(400);
        else ctx.json(transactionService.getAll(acc));
    };
}