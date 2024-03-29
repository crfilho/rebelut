package controller;
import io.javalin.http.Handler;
import model.Transfer;
import service.ITransactionDataService;

public class TransactionController {

    private ITransactionDataService transactionService;

    public TransactionController(ITransactionDataService transactionService) {
        this.transactionService = transactionService;
    }

    public Handler deposit = ctx -> {
        try {
            long accountid = Long.valueOf(ctx.pathParam("accountid"));
            double sum = ctx.pathParam("sum", double.class).check(it -> it > 0).get();

            ctx.status(200).json(transactionService.deposit(sum, accountid));
        }
        catch(Exception e) {
            ctx.status(400).json(e.getMessage());
        }
    };

    public Handler withdraw = ctx -> {
        try {
            long accountid = Long.valueOf(ctx.pathParam("accountid"));
            double sum = ctx.pathParam("sum", double.class).check(it -> it > 0).get();

            ctx.status(200).json(transactionService.withdraw(sum, accountid));
        }
        catch(Exception e) {
            ctx.status(400).json(e.getMessage());
        }
    };

    public Handler transfer = ctx -> {

        try {
            long from = Long.valueOf(ctx.formParam("from"));
            long to = Long.valueOf(ctx.formParam("to"));
            double sum = ctx.formParam("sum", double.class).check(it -> it > 0).get();

            Transfer transfer = transactionService.transfer(sum, from, to);
            if (transfer.getOrigTransaction().getStatus() <= 0 || transfer.getDestTransaction().getStatus() <= 0)
                ctx.status(403).json(transfer);
            else
                ctx.status(200).json(transfer);
        }
        catch(Exception e) {
            ctx.status(400).json(e.getMessage());
        }
    };

    public Handler getTransactions = ctx -> {

        try {
            long accountid = Long.valueOf(ctx.pathParam("accountid"));
            ctx.json(transactionService.getAll(accountid));
        }
        catch(Exception e) {
            ctx.status(400).json(e.getMessage());
        }
    };
}