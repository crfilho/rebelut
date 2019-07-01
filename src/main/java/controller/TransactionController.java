package controller;
import io.javalin.http.Handler;
import model.Account;
import model.Transaction;
import service.IAccountDataService;
import service.ITransactionDataService;

import java.math.BigDecimal;

public class TransactionController {

    private ITransactionDataService transactionService;

    public TransactionController(ITransactionDataService transactionService) {
        this.transactionService = transactionService;
    }

    public Handler deposit = ctx -> {

        long accountid = Long.valueOf(ctx.pathParam("accountid"));
        double sum = ctx.pathParam("sum", double.class).check(it -> it > 0).get();

        ctx.json(transactionService.deposit(sum, accountid));
    };

    public Handler withdraw = ctx -> {

        long accountid = Long.valueOf(ctx.pathParam("accountid"));
        double sum = ctx.pathParam("sum", double.class).check(it -> it > 0).get();

        ctx.json(transactionService.withdraw(sum, accountid));
    };

    public Handler transfer = ctx -> {

        try {
            long from = Long.valueOf(ctx.formParam("from"));
            long to = Long.valueOf(ctx.formParam("to"));
            double sum = ctx.formParam("sum", double.class).check(it -> it > 0).get();

            Transaction t = transactionService.transfer(sum, from, to);
            if (t.getStatus() < 0) ctx.status(403).json(t);
            else ctx.status(200).json(t);
        }
        catch(Exception e) {
            ctx.status(400).json(e);
        }
    };

    public Handler getTransactions = ctx -> {

        long accountid = Long.valueOf(ctx.pathParam("accountid"));
        ctx.json(transactionService.getAll(accountid));
    };
}